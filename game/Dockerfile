# ==== Build asamasi ====
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests
# Uretilen jar'i sabit bir isme kopyala (target/*.jar wildcard sorununu onler)
RUN cp target/*.jar app.jar

# ==== Calistirma asamasi ====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
