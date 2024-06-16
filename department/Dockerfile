FROM amazoncorretto:17-alpine3.14-jdk
LABEL authors="dwrik"

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src

ENTRYPOINT ["./mvnw", "spring-boot:run"]
