# Fase di build: costruzione dell'applicazione con Maven
FROM openjdk:17-slim AS builder

# Aggiorna il sistema e installa Maven
RUN apt-get update && apt-get install -y maven

# Copia il progetto all'interno del container
COPY . .

# Compila il progetto con Maven
RUN mvn clean package -DskipTests

# Fase finale: immagine per l'esecuzione
FROM openjdk:17-jdk-slim

# Copia il file .jar dalla fase di build
COPY --from=builder /app/target/*.jar app.jar

# Esegui il file .jar
CMD ["java", "-jar", "app.jar"]
