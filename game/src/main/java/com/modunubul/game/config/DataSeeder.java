package com.modunubul.game.config;

import com.modunubul.game.model.CardImage;
import com.modunubul.game.model.Situation;
import com.modunubul.game.repository.CardImageRepository;
import com.modunubul.game.repository.SituationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Spring Boot ayağa kalkarken bu sınıfı otomatik çalıştırır
@Component
public class DataSeeder implements CommandLineRunner {

    private final SituationRepository situationRepository;
    private final CardImageRepository cardImageRepository;

    public DataSeeder(SituationRepository situationRepository, CardImageRepository cardImageRepository) {
        this.situationRepository = situationRepository;
        this.cardImageRepository = cardImageRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Eğer veritabanında hiç durum cümlesi yoksa, senin yazdıklarını ekler
        if (situationRepository.count() == 0) {
            // Buraya kendi komik durum cümlelerini virgülle ayırarak ekleyebilirsin
            List<String> mySituations = List.of(
                    "Claude limiti bitmiştir:",
                    "SPSS raporunu kaydetmeyi unutup her şeyi baştan yapmak zorunda kalmışsındır:",
                    "Tartıda HİÇ olmaması gereken bir rakam görmüşsündür:",
                    "Matching giyinmişizdir:",
                    "Yiğit 'ben bişi yaptım' demiştir:",
                    "Dilara geç kalmıştır:",
                    "Yiğit çak yapıp Dilara'nın elini havada bırakmıştır:",
                    "Swag burger masaya gelmiştir:",
                    "Konserin en beklediğin anında telefonunun hafızası dolmuştur:",
                    "Yiğit yoğunluktan Dilara ile konuşamıyordur:",
                    "Ortamda birisi magnolya demiştir:",
                    "Yiğit aşırı maço adam taklidi yapmştır ve bu Dilara'nın hoşuna gitmiştir:",
                    "Wanna'da yer kalmamıştır:",
                    "İlk ve Son izlerken biz:",
                    "Sadece ikimizin anladığı bir şakayı kalabalık ortamda yapıp sadece ikimiz gülmüşüzdür:",
                    "Yiğit ilk defa İngiltere olayından bahsetmiştir:"
            );

            for (String text : mySituations) {
                Situation s = new Situation();
                s.setText(text);
                situationRepository.save(s);
            }
            System.out.println("Kendi durum cümlelerin veritabanına eklendi!");
        }

        // Eğer veritabanında hiç fotoğraf yoksa, senin fotoğraflarını ekler
        if (cardImageRepository.count() == 0) {
            // Buraya static/images klasörüne attığın fotoğrafların BİREBİR dosya adlarını yazmalısın
            List<String> myImages = List.of(
                    "aka.JPEG",
                    "akraba.png",
                    "bune.JPG",
                    "dgdg.JPG",
                    "eller.JPEG",
                    "deneme.JPEG",
                    "forum.png",
                    "kedi.jpg",
                    "labirent.png",
                    "overthink.png",
                    "park.png",
                    "ramo.png",
                    "sus.JPEG",
                    "wanna.JPG",
                    "wtf.png",
                    "yatak.png"
            );

            for (String filename : myImages) {
                CardImage img = new CardImage();
                img.setFilename(filename);
                cardImageRepository.save(img);
            }
            System.out.println("Kendi fotoğrafların veritabanına eklendi!");
        }
    }
}