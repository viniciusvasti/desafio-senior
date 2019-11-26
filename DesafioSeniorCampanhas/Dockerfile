FROM openjdk:8-alpine

MAINTAINER Vinícius A. dos Santos

RUN apk update && apk add bash

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

COPY target/DesafioSeniorCampanhas-0.0.1-SNAPSHOT.jar $PROJECT_HOME/DesafioSeniorCampanhas.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-jar", "-Dspring.data.mongodb.uri=mongodb://mongocampanhas", "-Dspring.data.mongodb.database=dbcampanhas", "-Dspring.data.mongodb.port=27017" ,"./DesafioSeniorCampanhas.jar"]
