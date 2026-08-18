# Build stage
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jdk

WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Asia/Kolkata"

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]