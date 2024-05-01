FROM maven:3.9.6-amazoncorretto-17 AS build

WORKDIR /app

COPY . /app/

RUN mvn clean package

FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8002

CMD [ "java", "-Dspring.profiles.active=prod", "-jar", "app.jar" ]
