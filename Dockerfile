# Stage 1: Build
FROM maven:3.8.8-jdk-21 AS builder
WORKDIR /build

# Copia o pom.xml e baixa dependências primeiro (caching melhorado)
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY src ./src

# Build do projeto (pulando testes)
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM amazoncorretto:21-alpine3.21
WORKDIR /app

# Copia o artefato gerado pelo build
COPY --from=builder /build/target/*.jar app.jar

# Exponha a porta que seu app usa
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
