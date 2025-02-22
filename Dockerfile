FROM ubuntu:latest AS build

RUN apt-get update

Run apt-get install openjdk-17-jdk -y
COPY . .
RUN ./gradlew bootJar --no daemon

FROM openjdk:17-jdk-slim
EXPOSE 8080
COPY --from=build /app/build/libs/*.jar app.jar