# Stage 1: build
FROM maven:3.9.3-eclipse-temurin-21 AS builder
WORKDIR /build

# Copia arquivos do Maven
COPY pom.xml ./
COPY src ./src

# Build do projeto (skip testes se quiser)
RUN mvn clean package -DskipTests

# Stage 2: runtime
FROM amazoncorretto:21-alpine3.21
WORKDIR /app

# Copia o jar gerado pelo Maven
COPY --from=builder /build/target/*.jar app.jar

# Porta do Spring Boot
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
