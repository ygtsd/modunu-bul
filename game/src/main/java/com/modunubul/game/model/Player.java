package com.modunubul.game.model;

import lombok.Data;
import java.util.List;

// Oyuncu bilgilerini tutacağımız sınıf
@Data
public class Player {

    // Oyuncunun benzersiz kimliği
    private String playerId;

    // Retro çetele tablosu için güncel puanı
    private int score;

    // Oyuncunun elindeki 5 adet resmin listesi (Dosya adları veya URL'ler)
    private List<String> hand;

    // Bu turda kart seçip seçmediğini kontrol eden bayrak
    private boolean hasPlayedThisRound;
}