FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY ./build/libs/events-service-0.0.1-SNAPSHOT.jar /app/events-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/events-service.jar"]
