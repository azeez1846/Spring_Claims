FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY claims-common ./claims-common
COPY policy-service ./policy-service
COPY fnol-intake-service ./fnol-intake-service
COPY adjuster-assignment-service ./adjuster-assignment-service
COPY reserve-settlement-service ./reserve-settlement-service
COPY api-gateway ./api-gateway
RUN mvn clean package -DskipTests -pl policy-service -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/policy-service/target/*.jar app.jar
ENV PORT=8081
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
