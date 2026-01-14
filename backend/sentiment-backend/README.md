# Sentiment Backend - API de Análise de Sentimentos

API REST em Java/Spring Boot para análise de sentimentos, desenvolvida para o Hackathon One.

## 🚀 Funcionalidades

### MVP (Obrigatório)
- ✅ `POST /api/v1/sentiment` - Análise de sentimento de texto único
- ✅ Validação de entrada (texto mínimo de 3 caracteres)
- ✅ Resposta com previsão e probabilidade

### Funcionalidades Opcionais
- ✅ `GET /api/v1/stats` - Estatísticas de análises
- ✅ `POST /api/v1/sentiment/batch` - Processamento em lote
- ✅ Persistência em banco de dados (H2/PostgreSQL)
- ✅ Interface web para testes
- ✅ Docker e Docker Compose
- ✅ Testes automatizados (unitários e integração)

## 📋 Pré-requisitos

- Java 17+
- Maven 3.9+
- Docker e Docker Compose (para produção)

## 🏃 Executando Localmente

### Modo Desenvolvimento (H2 em memória)

```bash
cd backend/sentiment-backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

A aplicação estará disponível em:
- API: http://localhost:8080
- Interface Web: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console

### Modo Produção (Docker)

```bash
# Na raiz do projeto
docker-compose up -d --build
```

## 📡 Endpoints da API

### Análise de Sentimento (MVP)

```http
POST /api/v1/sentiment
Content-Type: application/json

{
  "text": "Este produto é excelente! Recomendo a todos."
}
```

**Resposta:**
```json
{
  "previsao": "Positivo",
  "probabilidade": 0.92
}
```

### Análise em Lote (Batch)

```http
POST /api/v1/sentiment/batch
Content-Type: application/json

{
  "texts": [
    {"text": "Produto excelente!"},
    {"text": "Péssima experiência"},
    {"text": "Produto normal"}
  ]
}
```

**Resposta:**
```json
{
  "batch_id": "uuid-gerado",
  "total": 3,
  "resultados": [
    {"texto": "Produto excelente!", "previsao": "Positivo", "probabilidade": 0.95},
    {"texto": "Péssima experiência", "previsao": "Negativo", "probabilidade": 0.88},
    {"texto": "Produto normal", "previsao": "Neutro", "probabilidade": 0.65}
  ],
  "tempo_total_ms": 150
}
```

### Estatísticas

```http
GET /api/v1/stats
```

**Resposta:**
```json
{
  "total_analises": 100,
  "positivos": 60,
  "negativos": 30,
  "neutros": 10,
  "percentual_positivos": 60.0,
  "percentual_negativos": 30.0,
  "percentual_neutros": 10.0,
  "probabilidade_media_positivos": 0.89,
  "probabilidade_media_negativos": 0.85,
  "tempo_medio_processamento_ms": 45.5
}
```

### Health Check

```http
GET /api/v1/health
```

**Resposta:**
```json
{
  "status": "UP",
  "service": "sentiment-backend",
  "dependencies": {
    "ds-service": "UP"
  }
}
```

## 🧪 Testes

### Executar todos os testes

```bash
./mvnw test
```

### Executar com cobertura

```bash
./mvnw test jacoco:report
```

## 🐳 Docker

### Build da imagem

```bash
docker build -t sentiment-backend .
```

### Executar container

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DS_SERVICE_URL=http://ds-service:8000 \
  -e DATABASE_URL=jdbc:postgresql://postgres:5432/sentimentdb \
  -e DATABASE_USER=sentiment_user \
  -e DATABASE_PASSWORD=sentiment_pass \
  sentiment-backend
```

## 🔧 Configuração

### Variáveis de Ambiente

| Variável | Descrição | Padrão |
|----------|-----------|--------|
| `SPRING_PROFILES_ACTIVE` | Profile ativo (dev/prod) | dev |
| `DS_SERVICE_URL` | URL do serviço de ML | http://localhost:8000 |
| `DATABASE_URL` | URL de conexão JDBC | H2 em memória |
| `DATABASE_USER` | Usuário do banco | sa |
| `DATABASE_PASSWORD` | Senha do banco | (vazio) |

## 📁 Estrutura do Projeto

```
sentiment-backend/
├── src/main/java/com/sentimentapi/
│   ├── SentimentApiApplication.java
│   ├── config/           # Configurações (CORS, RestTemplate)
│   ├── controller/       # Controllers REST
│   ├── domain/
│   │   ├── entity/       # Entidades JPA
│   │   └── enums/        # Enums de domínio
│   ├── dto/
│   │   ├── request/      # DTOs de entrada
│   │   └── response/     # DTOs de saída
│   ├── exception/        # Tratamento de exceções
│   ├── repository/       # Repositórios JPA
│   └── service/          # Lógica de negócio
├── src/main/resources/
│   ├── static/           # Interface web
│   ├── application.yml
│   ├── application-dev.yml
│   └── application-prod.yml
├── src/test/java/        # Testes
├── Dockerfile
└── pom.xml
```

## 📚 Documentação

- [Swagger UI](http://localhost:8080/swagger-ui.html) - Documentação interativa da API
- [API Docs](http://localhost:8080/api-docs) - OpenAPI JSON

## 🤝 Integração com DS Service

O backend se comunica com o microserviço de Data Science (FastAPI) através do endpoint:

```
POST http://ds-service:8000/predict
Body: { "text": "..." }
Response: { "label": "Positivo", "probability": 0.92 }
```

## 📄 Licença

MIT License - Hackathon One
