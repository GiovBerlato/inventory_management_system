FROM maven:3.9.12-eclipse-temurin-25 AS build

WORKDIR /app
COPY src ./src
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:25-jre-alpine

WORKDIR /ims-api

COPY --from=build /app/target/inventory-management-system-2.0.1.jar IMSApplication.jar

CMD ["java", "-jar", "IMSApplication.jar"]