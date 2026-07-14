package com.modunubul.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// Bu sınıfın bir ayar sınıfı olduğunu Spring'e bildiriyoruz
@Configuration
// WebSocket üzerinden mesajlaşmayı aktifleştiriyoruz
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Frontend'in WebSocket'e bağlanacağı ana kapıyı (endpoint) belirliyoruz
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Bağlantı adresi
                .setAllowedOriginPatterns("*") // CORS ayarı: Şimdilik her yerden erişime açık
                .withSockJS(); // Bağlantı kopmalarına karşı yedek mekanizma
    }

    // Mesajların hangi yollardan gidip geleceğini ayarlıyoruz
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Sunucunun frontend'e mesaj göndereceği kanalın ön eki
        registry.enableSimpleBroker("/topic");
        // Frontend'in sunucuya mesaj gönderirken kullanacağı ön ek
        registry.setApplicationDestinationPrefixes("/app");
    }
}