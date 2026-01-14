# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia arquivos de dependência primeiro (cache de camadas)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Cria usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Instala curl para health check
RUN apk add --no-cache curl

# Copia o JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

# Ajusta permissões
RUN chown -R appuser:appgroup /app
USER appuser

# Expõe a porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Configurações de JVM para containers
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
