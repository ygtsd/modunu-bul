package com.modunubul.game.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GameState {

    private String roomId;
    private String status;
    private int currentRound;
    private String currentSituation;
    private List<Player> players;
    private Map<String, String> table;

    // Oyun odasına ait ortak kart destesi
    private List<String> deck;
}