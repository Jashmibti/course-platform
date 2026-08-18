# Build inside Docker so Maven/JDK do not need to be installed on the host.
FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

RUN useradd --system --uid 10001 springuser
COPY --from=build /workspace/target/course-service-1.0.0.jar app.jar
RUN chown -R springuser:springuser /app

USER 10001
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
