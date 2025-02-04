# Usa un'immagine di base Maven per costruire il progetto
FROM maven:3.8.6-openjdk-17-slim AS builder

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=builder /app/target/*.jar  app.jar

# Esegui il file .jar
CMD ["java", "-jar", "app.jar"]
