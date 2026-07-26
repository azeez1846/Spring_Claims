FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build
COPY pom.xml .
COPY claims-common ./claims-common
COPY api-gateway ./api-gateway
COPY policy-service ./policy-service
COPY fnol-intake-service ./fnol-intake-service
COPY adjuster-assignment-service ./adjuster-assignment-service
COPY reserve-settlement-service ./reserve-settlement-service
RUN mvn clean package -DskipTests -pl api-gateway -am

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/api-gateway/target/*.jar app.jar
ENV PORT=8090
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
