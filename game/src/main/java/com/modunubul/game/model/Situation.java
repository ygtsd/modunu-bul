package com.modunubul.game.model;

import jakarta.persistence.*;
import lombok.Data;

// Bu sınıfın veritabanında bir tablo olacağını belirtiyoruz
@Entity
@Data
@Table(name = "situations")
public class Situation {

    // Primary Key (Birincil Anahtar) ve otomatik artan ID ayarı
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Durum metni (Uzun olabileceği için length değerini yüksek tutuyoruz)
    @Column(nullable = false, length = 500)
    private String text;
}