# hackathon-one-sentiment-api

Monorepositório do hackathon: API de análise de sentimento (Back-end + Data Science).

## Estrutura
- backend/      -> Spring Boot (API pública: POST /sentiment)
- ds-service/   -> Python (inferência: POST /predict)
- datascience/  -> Notebook + scripts de treino + export do modelo
- contracts/    -> Contratos e exemplos de payloads
- docs/         -> Documentos auxiliares

## Contrato (MVP)
### POST /sentiment (Back-end)
Request:
{ "text": "..." }

Response:
{ "previsao": "Positivo", "probabilidade": 0.87 }

## Execução
(Em breve: docker-compose para subir BE + DS em um comando)
