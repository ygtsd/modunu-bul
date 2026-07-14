# Java 17 ortamını kullan
FROM eclipse-temurin:17-jdk-jammy

# Proje dosyalarını kopyala
COPY . .

# Maven ile paketi oluştur
RUN ./mvnw clean package -DskipTests

# Uygulamayı çalıştır (target klasöründeki .jar dosyasını bulur)
CMD ["java", "-jar", "target/*.jar"]