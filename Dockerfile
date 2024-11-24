FROM maven:3.8-openjdk-17 as build
WORKDIR /app
COPY library-app/ .
RUN mvn clean install -DskipTests


FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/library-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE ${API_LISTENING_PORT}

ENTRYPOINT ["java", "-jar", "/app/app.jar"]

CMD ["--server.port=${API_LISTENING_PORT}", \
     "--spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}", \
     "--spring.datasource.username=${DB_USERNAME}", \
     "--spring.datasource.password=${DB_PASSWORD}", \
     "--admin.api.key=${ADMIN_API_KEY}"]
