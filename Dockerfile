# Stage 1: Build
FROM maven:3.9.2-eclipse-temurin-21 AS builder
WORKDIR /build

# Copia apenas o pom.xml primeiro para cache de dependências
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Build do projeto (skip tests)
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM amazoncorretto:21-alpine3.21
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
