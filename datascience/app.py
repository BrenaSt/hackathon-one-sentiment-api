from fastapi import FastAPI
from pydantic import BaseModel
import joblib

# Criando a aplicação
app = FastAPI()

# Carregando modelo e vetorizador
model = joblib.load("sentiment_model.pkl")
vectorizer = joblib.load("tfidf_vectorizer.pkl")

# Estrutura do dado de entrada
class TextInput(BaseModel):
    text: str

# Endpoint principal
@app.post("/predict")
def predict_sentiment(data: TextInput):
    text_vectorized = vectorizer.transform([data.text])
    prediction = model.predict(text_vectorized)[0]

    return {
        "sentiment_prediction": int(prediction)
    }
