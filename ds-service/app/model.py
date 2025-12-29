import os
from dataclasses import dataclass
from typing import Optional

import joblib


@dataclass(frozen=True)
class ModelResult:
    label: str
    probability: float


class SentimentModel:
    """
    Wrapper do pipeline de ML (scikit-learn).
    - Carrega um arquivo .joblib se existir.
    - Se não existir, usa fallback heurístico para não bloquear a integração.
    """

    def __init__(self, model_path: str):
        self.model_path = model_path
        self.pipeline: Optional[object] = None

    def load(self) -> None:
        if os.path.exists(self.model_path):
            self.pipeline = joblib.load(self.model_path)
        else:
            self.pipeline = None

    def predict(self, text: str) -> ModelResult:
        if self.pipeline is None:
            lowered = text.lower()
            negative_markers = ["ruim", "péssim", "horr", "defeito", "demor", "atras", "não recomendo"]
            if any(m in lowered for m in negative_markers):
                return ModelResult(label="Negativo", probability=0.85)
            return ModelResult(label="Positivo", probability=0.75)

        proba = self.pipeline.predict_proba([text])[0]
        classes = list(self.pipeline.classes_)
        best_idx = int(proba.argmax())
        return ModelResult(label=str(classes[best_idx]), probability=float(proba[best_idx]))
