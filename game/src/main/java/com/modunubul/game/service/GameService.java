package com.modunubul.game.service;

import com.modunubul.game.model.CardImage;
import com.modunubul.game.model.GameState;
import com.modunubul.game.model.Player;
import com.modunubul.game.model.Situation;
import com.modunubul.game.repository.CardImageRepository;
import com.modunubul.game.repository.SituationRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final HashMap<String, GameState> activeGames = new HashMap<>();
    private final SituationRepository situationRepository;
    private final CardImageRepository cardImageRepository;

    public GameService(SituationRepository situationRepository, CardImageRepository cardImageRepository) {
        this.situationRepository = situationRepository;
        this.cardImageRepository = cardImageRepository;
    }

    public GameState createNewGame() {
        GameState newGame = new GameState();
        String roomId = UUID.randomUUID().toString().substring(0, 8);
        newGame.setRoomId(roomId);
        newGame.setStatus("WAITING");
        newGame.setCurrentRound(1);
        newGame.setPlayers(new ArrayList<>());
        newGame.setTable(new HashMap<>());

        // Oyun başladığında tüm fotoğrafları veritabanından çek ve karıştırarak desteye koy
        List<CardImage> allImages = cardImageRepository.findAll();
        List<String> deck = allImages.stream()
                .map(CardImage::getFilename)
                .collect(Collectors.toList());
        Collections.shuffle(deck); // Desteyi fiziksel olarak karıştır
        newGame.setDeck(deck);

        activeGames.put(roomId, newGame);
        return newGame;
    }

    public GameState joinGame(String roomId, String playerId) {
        GameState game = activeGames.get(roomId);
        if (game == null) return null;

        if (game.getPlayers().size() < 2) {
            Player newPlayer = new Player();
            newPlayer.setPlayerId(playerId);
            newPlayer.setScore(0);
            newPlayer.setHasPlayedThisRound(false);

            // Veritabanı yerine, bu odaya özel oluşturduğumuz desteden 4 kart çek
            newPlayer.setHand(drawCardsFromDeck(game, 4));
            game.getPlayers().add(newPlayer);
        }

        if (game.getPlayers().size() == 2 && game.getStatus().equals("WAITING")) {
            game.setStatus("PLAYING");
            game.setCurrentSituation(getRandomSituationText());
        }

        return game;
    }

    public GameState playCard(String roomId, String playerId, String cardImage) {
        GameState game = activeGames.get(roomId);
        if (game == null) return null;

        for (Player p : game.getPlayers()) {
            if (p.getPlayerId().equals(playerId)) {
                p.setHasPlayedThisRound(true);
                p.getHand().remove(cardImage);
                break;
            }
        }

        game.getTable().put(playerId, cardImage);

        if (game.getTable().size() == 2) {
            game.setStatus("VOTING");
        }

        return game;
    }

    public GameState voteCard(String roomId, String votedPlayerId) {
        GameState game = activeGames.get(roomId);
        if (game == null) return null;

        for (Player p : game.getPlayers()) {
            if (p.getPlayerId().equals(votedPlayerId)) {
                p.setScore(p.getScore() + 1);
                break;
            }
        }

        game.getTable().clear();
        for (Player p : game.getPlayers()) {
            p.setHasPlayedThisRound(false);
        }

        int nextRound = game.getCurrentRound() + 1;
        if (nextRound > 16) {
            game.setStatus("FINISHED");
            game.setCurrentSituation("Oyun Bitti! Kazanan belirleniyor...");
        } else {
            game.setCurrentRound(nextRound);
            game.setStatus("PLAYING");
            game.setCurrentSituation(getRandomSituationText());

            if (game.getPlayers().get(0).getHand().isEmpty()) {
                for (Player p : game.getPlayers()) {
                    // Yeni kart dağıtımında yine ortak desteden çekim yap
                    p.setHand(drawCardsFromDeck(game, 4));
                }
            }
        }

        return game;
    }

    // Desteden kart çeken ve çekilen kartı desteden eksilten yardımcı metod
    private List<String> drawCardsFromDeck(GameState game, int count) {
        List<String> drawn = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // Eğer destede hala kart varsa en üsttekini (0. indeksi) çek ve desteden çıkar
            if (!game.getDeck().isEmpty()) {
                drawn.add(game.getDeck().remove(0));
            }
        }
        return drawn;
    }

    private String getRandomSituationText() {
        List<Situation> situations = situationRepository.findRandomSituations(1);
        if (!situations.isEmpty()) {
            return situations.get(0).getText();
        }
        return "Durum bulunamadı (Veritabanı boş olabilir!)";
    }
}