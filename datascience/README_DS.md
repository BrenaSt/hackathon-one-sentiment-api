# 📊 Data Science – SentimentAPI

Este diretório contém toda a **etapa de Data Science** do projeto **SentimentAPI**, desenvolvido durante o Hackathon. O objetivo desta etapa é **construir, avaliar e disponibilizar um modelo de Machine Learning para análise de sentimentos**, que posteriormente é consumido pelo backend via API.

O foco do trabalho foi criar um **MVP funcional**, utilizando técnicas clássicas de NLP e aprendizado supervisionado, com integração clara e desacoplada em relação ao backend.

---

## 🧠 Visão Geral da Abordagem

* Linguagem: **Python**
* Técnicas principais:

  * Processamento de Linguagem Natural (NLP)
  * Vetorização de texto com **TF-IDF**
  * Modelos de **Regressão Logística**
* Tipos de classificação testados:

  * **Binária** (Positivo / Negativo)
  * **Ternária** (Positivo / Neutro / Negativo)

---

## 📁 Estrutura dos Notebooks

### 📘 Notebook 1 – Exploração e Preparação do DataSet

**Objetivo:** preparar os dados para o treinamento dos modelos.

Principais etapas:

* Carregamento do dataset de avaliações/comentários
* Análise exploratória dos dados (EDA)
* Limpeza de dados:

  * Remoção de valores nulos
  * Padronização de textos
* Criação da variável alvo:

  * Conversão de notas em rótulos de sentimento
* Análise de balanceamento das classes
* Geração do dataset final limpo e organizado para modelagem

📌 Resultado: dataset pronto para treinamento supervisionado.

---

### 📗 Notebook 2 – Treinamento do Modelo Binário

**Objetivo:** treinar e avaliar um modelo de classificação binária de sentimentos.

Principais etapas:

* Definição do problema: **Positivo vs Negativo**
* Separação dos dados em treino e teste
* Vetorização dos textos com **TF-IDF**
* Treinamento do modelo de **Regressão Logística**
* Avaliação do modelo com:

  * Acurácia
  * Precisão
  * Recall
  * F1-score
  * Matriz de confusão
* Testes manuais com frases reais

📌 Resultado: modelo binário com desempenho consistente, considerado o mais adequado para o MVP.

---

### 📙 Notebook 3 – Avaliando Desempenho do Modelo Ternário

**Objetivo:** avaliar a viabilidade de um modelo ternário incluindo a classe **Neutra**.

Principais etapas:

* Definição do problema ternário (Negativo / Neutro / Positivo)
* Análise de desbalanceamento entre classes
* Aplicação de técnicas de balanceamento (class_weight e undersampling)
* Treinamento do modelo ternário
* Avaliação detalhada por classe
* Comparação com o modelo binário
* Análise crítica dos resultados

📌 Conclusão: o modelo ternário apresentou desempenho inferior, principalmente na classe neutra, sendo considerado menos estável para o MVP.

---

## 📦 Serialização do Modelo

Após o treinamento, os seguintes artefatos foram gerados e salvos:

* Modelo treinado (`LogisticRegression`)
* Vetorizador TF-IDF ajustado

Esses objetos foram serializados utilizando a biblioteca **joblib**, permitindo que o modelo seja reutilizado sem necessidade de novo treinamento.

---

## 🔌 Integração com o Back-End

O modelo de análise de sentimento foi treinado em um notebook de Data Science e serializado utilizando o **joblib**, gerando arquivos contendo o modelo treinado e o vetorizador TF-IDF.

Para a integração com o backend, foi criada uma API utilizando **FastAPI**. Essa API é responsável por carregar os arquivos serializados com joblib no momento da inicialização, sem necessidade de novo treinamento.

A FastAPI expõe um endpoint que:

* Recebe um texto via requisição HTTP em formato JSON
* Aplica o mesmo processo de vetorização utilizado no treinamento
* Utiliza o modelo carregado para realizar a predição do sentimento
* Retorna a previsão e a probabilidade associada

Dessa forma, a etapa de Data Science fica **desacoplada do backend**, permitindo que o backend consuma as predições apenas via requisição HTTP, garantindo simplicidade, escalabilidade e separação de responsabilidades.

---

## 📌 Observações Finais

* O projeto prioriza **clareza metodológica** e **aplicabilidade prática**
* O modelo binário foi escolhido como solução principal do MVP
* O modelo ternário permanece como possibilidade de evolução futura
* Melhorias futuras podem incluir:

  * Ampliação do dataset
  * Modelos baseados em embeddings ou transformers
  * Análise de sentimentos multilíngue

---

📍 **Projeto desenvolvido para fins educacionais e demonstrativos durante o Hackathon.**

---

Se quiser, posso:

* Criar um README **mais curto**
* Adaptar o texto para **inglês**
* Ou alinhar com o README geral do projeto

É só me dizer 😊
