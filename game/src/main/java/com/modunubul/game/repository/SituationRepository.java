package com.modunubul.game.repository;

import com.modunubul.game.model.Situation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// JpaRepository sayesinde temel CRUD (Ekle, Sil, Güncelle, Getir) işlemleri hazır gelir
@Repository
public interface SituationRepository extends JpaRepository<Situation, Long> {

    // Veritabanından rastgele n adet durum getiren özel SQL sorgusu
    @Query(value = "SELECT * FROM situations ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Situation> findRandomSituations(int limit);
}