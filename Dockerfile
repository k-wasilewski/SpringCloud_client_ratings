FROM openjdk:8-jdk-alpine

MAINTAINER Kuba Wasilewski <k.k.wasilewski@gmail.com>

VOLUME /tmp

EXPOSE 8086

ARG JAR_FILE=target/client_ratings-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} client_ratings.jar

ENTRYPOINT ["java","-jar","/client_ratings.jar"]
