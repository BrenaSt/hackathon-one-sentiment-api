# Build stage
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Cria usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Variáveis de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=prod
ENV DS_SERVICE_URL=http://ds-service:8000
ENV DATABASE_URL=jdbc:postgresql://postgres:5432/sentimentdb
ENV DATABASE_USER=sentiment_user
ENV DATABASE_PASSWORD=sentiment_pass

# Expõe a porta do Spring Boot
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
