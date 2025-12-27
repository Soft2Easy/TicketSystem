FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY ./build/libs/registration-service-0.0.1-SNAPSHOT.jar /app/registration-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/registration-service.jar"]