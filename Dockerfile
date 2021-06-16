FROM openjdk:11-slim-buster

EXPOSE 8081
ARG JARFILE=target/*.jar
COPY ${JARFILE} app.jar
CMD ["java", "-jar", "/app.jar"]