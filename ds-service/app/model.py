import os
from dataclasses import dataclass
from typing import Optional, Tuple

import joblib


@dataclass(frozen=True)
class ModelResult:
    label: str
    probability: float


class SentimentModel:
    """
    Wrapper do modelo/pipeline do scikit-learn.
    - Se existir um arquivo joblib, carrega.
    - Se não existir, usa um fallback simples (para destravar integração do BE).
    """

    def __init__(self, model_path: str):
        self.model_path = model_path
        self.pipeline = None

    def load(self) -> None:
        if os.path.exists(self.model_path):
            self.pipeline = joblib.load(self.model_path)
        else:
            self.pipeline = None  # fallback

    def predict(self, text: str) -> ModelResult:
        if self.pipeline is None:
            # Fallback simples: útil para testar a API e a integração antes do modelo real.
            lowered = text.lower()
            negative_markers = ["ruim", "péssim", "horr", "defeito", "demor", "atras", "não recomendo"]
            if any(m in lowered for m in negative_markers):
                return ModelResult(label="Negativo", probability=0.85)
            return ModelResult(label="Positivo", probability=0.75)

        # Pipeline do scikit-learn: espera lista de textos
        proba = self.pipeline.predict_proba([text])[0]
        classes = list(self.pipeline.classes_)
        best_idx = int(proba.argmax())
        label = str(classes[best_idx])
        probability = float(proba[best_idx])
        return ModelResult(label=label, probability=probability)
