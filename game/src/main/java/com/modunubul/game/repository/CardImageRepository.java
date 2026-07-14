package com.modunubul.game.repository;

import com.modunubul.game.model.CardImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardImageRepository extends JpaRepository<CardImage, Long> {

    // Veritabanından rastgele n adet fotoğraf adı getiren sorgu
    @Query(value = "SELECT * FROM card_images ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<CardImage> findRandomImages(int limit);
}