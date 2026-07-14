package com.modunubul.game.controller;

import com.modunubul.game.model.GameState;
import com.modunubul.game.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Bu sınıfın dışarıdan gelen HTTP isteklerini karşılayacağını belirtiyoruz
@RestController
@RequestMapping("/api/game") // İsteklerin atılacağı ana URL
public class GameController {

    // Servisimizi buraya dahil ediyoruz
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Yeni oyun kurmak için POST isteği atılacak uç nokta
    @PostMapping("/create")
    public ResponseEntity<GameState> createGame() {
        // Servisteki yeni oyun oluşturma metodunu çağırıp frontend'e gönderiyoruz
        GameState newGame = gameService.createNewGame();
        return ResponseEntity.ok(newGame);
    }
}