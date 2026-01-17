# 📊 Data Science – SentimentAPI

Este diretório contém toda a etapa de **Data Science** do projeto **SentimentAPI**, desenvolvida durante o Hackathon ONE, com foco na construção, avaliação e disponibilização de um modelo de **análise de sentimentos em português**, integrado posteriormente a uma API de backend.

O objetivo principal foi aplicar, de forma prática, os conhecimentos adquiridos em **pré-processamento de dados, modelagem de Machine Learning, avaliação de desempenho e integração com sistemas backend**.

**Equipe de Data Science:**  
- Brena Stephany: BrenaSt
- Eyka Albuquerque: eikyalbuquerque
- Leticia

---

## 🧠 Visão Geral da Solução de Data Science

A solução de Data Science consiste em:

* Exploração e preparação de um dataset de avaliações textuais;
* Construção de modelos de classificação de sentimentos utilizando **TF-IDF + Regressão Logística**;
* Avaliação comparativa entre abordagens **binária** e **ternária**;
* Serialização do modelo final para integração com o backend via API.

---

## 📁 Estrutura dos Notebooks

### 📓 Notebook 1 – Exploração e Preparação do DataSet

Responsável pelas etapas iniciais do projeto:

* Leitura e inspeção do dataset;
* Limpeza dos dados textuais;
* Análise exploratória (EDA);
* Criação da variável alvo a partir das notas de avaliação;
* Tratamento de valores nulos e remoção de observações inconsistentes;
* Análise de balanceamento das classes;
* Geração do dataset final limpo para modelagem.

📌 **Decisão importante**:
As avaliações neutras foram removidas no modelo binário para reduzir ambiguidade e melhorar o aprendizado supervisionado.

---

### 📓 Notebook 2 – Treinamento do Modelo Binário

Neste notebook foi desenvolvido o **modelo final escolhido para o MVP**:

* Separação entre dados de treino e teste;
* Vetorização dos textos com **TF-IDF**;
* Treinamento de um modelo de **Regressão Logística Binária (Positivo / Negativo)**;
* Avaliação com métricas:

  * Acurácia
  * Precisão
  * Recall
  * F1-score
  * Matriz de confusão
* Testes manuais com frases reais;
* Serialização do modelo e do vetorizador utilizando **joblib**.

📌 **Resultado**:
O modelo apresentou desempenho consistente, métricas equilibradas e maior confiabilidade para uso em produção como MVP.

---

### 📓 Notebook 3 – Avaliando Desempenho do Modelo Ternário

Este notebook teve caráter **experimental e comparativo**, com foco em aprendizado e validação de hipóteses:

* Inclusão da classe **Neutra**;
* Treinamento de um modelo ternário (Negativo / Neutro / Positivo);
* Aplicação de técnicas de balanceamento;
* Avaliação detalhada das métricas por classe;
* Análise de limitações do modelo.

📌 **Conclusão**:
Apesar de ajustes e balanceamento, o modelo ternário apresentou desempenho inferior ao binário, especialmente na classe neutra, que possui fronteiras semânticas menos definidas. Por esse motivo, o modelo binário foi adotado como solução final do projeto.

---

## ⚙️ Integração com o Backend

O modelo de análise de sentimento foi treinado em ambiente de Data Science e **serializado utilizando a biblioteca `joblib`**, gerando arquivos contendo:

* O modelo treinado (Regressão Logística);
* O vetorizador TF-IDF ajustado no treinamento.

Para a integração com o backend:

* Foi desenvolvida uma API utilizando **FastAPI**;
* A API carrega os arquivos serializados no momento da inicialização;
* Não há necessidade de novo treinamento em produção;
* A API expõe um endpoint que:

  * Recebe um texto via requisição HTTP em formato JSON;
  * Aplica a mesma vetorização TF-IDF usada no treinamento;
  * Retorna a predição do sentimento e a probabilidade associada.

Essa arquitetura garante:

* Separação clara de responsabilidades entre DS e Backend;
* Simplicidade de manutenção;
* Escalabilidade da solução.

---

## 🎯 Justificativa da Escolha do Modelo Binário

A escolha pelo modelo de **Regressão Logística Binária** foi motivada por:

* Melhor desempenho geral nas métricas de avaliação;
* Maior estabilidade nas previsões;
* Redução de ambiguidades semânticas;
* Maior aderência ao objetivo de um **MVP funcional**;
* Facilidade de interpretação e integração com o backend.

O modelo ternário foi explorado como estudo complementar, mas apresentou limitações relacionadas à classe neutra, comum em problemas de análise de sentimentos com modelos lineares e datasets de tamanho moderado.

---

## 🛠️ Tecnologias Utilizadas

* Python
* Pandas
* NumPy
* scikit-learn
* Matplotlib / Seaborn
* joblib
* Jupyter Notebook / Google Colab

---

## 📌 Observação Final

Este módulo de Data Science foi desenvolvido com foco em **aprendizado prático, clareza metodológica e integração real com backend**, atendendo aos objetivos do Hackathon ONE e simulando um fluxo profissional de desenvolvimento de soluções baseadas em dados.

---

📬 Para mais detalhes sobre a API e execução do sistema completo, consulte a documentação da pasta **Backend**.
