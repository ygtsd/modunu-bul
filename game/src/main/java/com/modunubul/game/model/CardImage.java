package com.modunubul.game.model;

import jakarta.persistence.*;
import lombok.Data;

// Fotoğrafların dosya adlarını tutacağımız tablo
@Entity
@Data
@Table(name = "card_images")
public class CardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fotoğrafın dosya adı (örneğin: komik_yuz.jpg)
    @Column(nullable = false)
    private String filename;
}