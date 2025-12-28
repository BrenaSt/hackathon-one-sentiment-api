from fastapi import FastAPI, HTTPException
from .schemas import PredictRequest, PredictResponse

app = FastAPI(title="ds-service", version="0.1.0")


@app.get("/health")
def health():
    return {"status": "ok"}


@app.post("/predict", response_model=PredictResponse)
def predict(req: PredictRequest):
    text = (req.text or "").strip()
    if len(text) < 3:
        raise HTTPException(status_code=400, detail="Campo 'text' deve ter pelo menos 3 caracteres.")

    lowered = text.lower()
    negative_markers = ["ruim", "péssim", "horr", "defeito", "demor", "atras", "não recomendo"]

    if any(m in lowered for m in negative_markers):
        return PredictResponse(label="Negativo", probability=0.85)

    return PredictResponse(label="Positivo", probability=0.75)
