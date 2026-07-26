FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY claims-common ./claims-common
COPY policy-service ./policy-service
COPY fnol-intake-service ./fnol-intake-service
COPY adjuster-assignment-service ./adjuster-assignment-service
COPY reserve-settlement-service ./reserve-settlement-service
COPY api-gateway ./api-gateway
RUN mvn clean package -DskipTests -pl reserve-settlement-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/reserve-settlement-service/target/*.jar app.jar
ENV PORT=8084
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
