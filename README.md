# Sentiment API - Análise de Sentimentos

API para análise de sentimentos em textos utilizando Machine Learning, desenvolvida para o Hackathon One.

## Visão Geral

Este projeto implementa uma API REST que classifica sentimentos de textos (comentários, avaliações, tweets) como **Positivo**, **Negativo** ou **Neutro**, utilizando um modelo de Machine Learning treinado com TF-IDF e Regressão Logística.

### Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                        Frontend Web                          │
│                   (HTML/CSS/JavaScript)                      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     Backend (Spring Boot)                    │
│                         Java 17                              │
│              POST /api/v1/sentiment                          │
│              POST /api/v1/comentarios                        │
│              GET  /api/v1/dashboard/stats/{id}               │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┴───────────────┐
              ▼                               ▼
┌─────────────────────────┐     ┌─────────────────────────┐
│    DS Service (FastAPI) │     │   PostgreSQL Database   │
│       Python 3.11       │     │                         │
│    POST /predict        │     │   Clientes, Produtos,   │
│                         │     │   Comentários, etc.     │
└─────────────────────────┘     └─────────────────────────┘
```

## Tecnologias

| Componente | Tecnologia |
|------------|------------|
| Backend | Java 17, Spring Boot 3.2, Spring Data JPA |
| Data Science | Python 3.11, FastAPI, scikit-learn |
| Banco de Dados | PostgreSQL 15 (prod) / H2 (dev) |
| Frontend | HTML5, CSS3, JavaScript |
| Containerização | Docker, Docker Compose |
| Cloud | Oracle Cloud Infrastructure (OCI) |

## Estrutura do Projeto

```
hackathon-one-sentiment-api/
├── backend/
│   └── sentiment-backend/     # API Spring Boot
├── ds-service/                # Serviço de ML (FastAPI)
├── datascience/               # Notebooks e modelo treinado
├── frontend/
│   └── web/                   # Interface web
├── ddl/                       # Scripts SQL
├── docs/                      # Documentação
├── nginx/                     # Configuração do proxy
├── scripts/                   # Scripts de deploy
├── docker-compose.yml         # Orquestração de containers
└── README.md
```

## Início Rápido

### Pré-requisitos

- Docker e Docker Compose instalados
- Git

### Executando Localmente

1. Clone o repositório:
```bash
git clone https://github.com/AndreTeixeir/hackathon-one-sentiment-api.git
cd hackathon-one-sentiment-api
```

2. Copie o arquivo de ambiente:
```bash
cp .env.example .env
```

3. Inicie os containers:
```bash
docker-compose up -d --build
```

4. Acesse a aplicação:
- Frontend: http://localhost
- API: http://localhost:8080/api/v1/sentiment
- DS Service: http://localhost:8000/health

### Executando em Desenvolvimento (sem Docker)

**Backend:**
```bash
cd backend/sentiment-backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

**DS Service:**
```bash
cd ds-service
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

## Endpoints da API

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

### Outros Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/v1/clientes` | Criar cliente |
| GET | `/api/v1/clientes` | Listar clientes |
| POST | `/api/v1/produtos` | Criar produto |
| GET | `/api/v1/produtos` | Listar produtos |
| POST | `/api/v1/comentarios` | Criar comentário (com análise automática) |
| GET | `/api/v1/dashboard/stats/{vendedorId}` | Estatísticas do vendedor |
| GET | `/api/v1/notificacoes/vendedor/{vendedorId}` | Notificações |

## Deploy na OCI

### 1. Criar VM Compute (Free Tier)

- Shape: VM.Standard.A1.Flex (ARM) ou VM.Standard.E2.1.Micro (AMD)
- OS: Ubuntu 22.04
- Configurar Security List para portas 80, 8080, 8000

### 2. Configurar a VM

```bash
# Conectar via SSH
ssh -i sua-chave.pem ubuntu@IP_PUBLICO

# Executar script de setup
curl -sSL https://raw.githubusercontent.com/SEU_USUARIO/hackathon-one-sentiment-api/main/scripts/setup-oci-vm.sh | bash
```

### 3. Deploy da Aplicação

```bash
# Após logout/login
cd ~/sentiment-api
git clone https://github.com/SEU_USUARIO/hackathon-one-sentiment-api.git .
./scripts/deploy-oci.sh
```

## Modelo de Machine Learning

O modelo de análise de sentimentos foi treinado utilizando:

- **Algoritmo:** Regressão Logística
- **Vetorização:** TF-IDF (Term Frequency-Inverse Document Frequency)
- **Dataset:** Avaliações de produtos em português
- **Métricas:** Acurácia ~85%, F1-Score ~0.84

Os arquivos do modelo estão em `ds-service/models/`:
- `sentiment.joblib` - Modelo treinado
- `tfidf_vectorizer.pkl` - Vetorizador TF-IDF

## Documentação Adicional

- [Arquitetura](docs/arquitetura.md)
- [Requisitos](docs/requisitos.md)
- [API Reference](docs/api-reference.md)
- [Diagramas UML](docs/uml/)

## Licença

Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.
