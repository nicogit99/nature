FROM maven:3.9-openjdk-17-slim AS builder

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=builder /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
