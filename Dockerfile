# Fase di build: costruzione dell'applicazione con Maven
FROM openjdk:17-slim AS builder

# Aggiorna il sistema e installa Maven
RUN apt-get update && apt-get install -y maven

# Copia il progetto nel container
COPY . /app

# Imposta la directory di lavoro per Maven
WORKDIR /app

# Compila il progetto con Maven
RUN mvn clean package -DskipTests


# Copia il file .jar dalla fase di build
COPY --from=builder /app/target/*.jar /app/app.jar

# Esegui il file .jar
CMD ["java", "-jar", "/app/app.jar"]
