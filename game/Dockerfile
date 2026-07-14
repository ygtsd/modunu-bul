# Java 17 ortamını kullan
FROM openjdk:17-jdk-slim

# Proje dosyalarını kopyala
COPY . .

# Maven ile paketi oluştur
RUN ./mvnw clean package -DskipTests

# Uygulamayı çalıştır (target klasöründeki .jar dosyasını bulur)
CMD ["java", "-jar", "target/*.jar"]