package com.modunubul.game.controller;

import com.modunubul.game.model.GameState;
import com.modunubul.game.service.GameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

// Bu sınıfın WebSocket mesajlarını yöneteceğini belirtiyoruz
@Controller
public class GameWebSocketController {

    private final GameService gameService;

    public GameWebSocketController(GameService gameService) {
        this.gameService = gameService;
    }

    // Bir oyuncu odaya katıldığında tetiklenecek uç nokta
    @MessageMapping("/game.join/{roomId}")
    // İşlem bittikten sonra güncel oyunu odadaki herkese (/topic/game/roomId kanalına) gönder
    @SendTo("/topic/game/{roomId}")
    public GameState joinGame(@DestinationVariable String roomId, @Payload String playerId) {
        return gameService.joinGame(roomId, playerId);
    }

    // Bir oyuncu masaya kart attığında tetiklenecek uç nokta
    @MessageMapping("/game.play/{roomId}")
    @SendTo("/topic/game/{roomId}")
    public GameState playCard(@DestinationVariable String roomId, @Payload Map<String, String> payload) {
        // Frontend'den JSON içinde playerId ve cardImage gelecek
        String playerId = payload.get("playerId");
        String cardImage = payload.get("cardImage");
        return gameService.playCard(roomId, playerId, cardImage);
    }

    // Oylama yapıldığında tetiklenecek uç nokta
    @MessageMapping("/game.vote/{roomId}")
    @SendTo("/topic/game/{roomId}")
    public GameState voteCard(@DestinationVariable String roomId, @Payload String votedPlayerId) {
        return gameService.voteCard(roomId, votedPlayerId);
    }
}