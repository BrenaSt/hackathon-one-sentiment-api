from pydantic import BaseModel, Field


class PredictRequest(BaseModel):
    text: str = Field(..., min_length=3, description="Texto a ser classificado")


class PredictResponse(BaseModel):
    label: str = Field(..., description="Classe prevista (Positivo/Negativo/Neutro)")
    probability: float = Field(..., ge=0.0, le=1.0, description="Probabilidade (0 a 1)")
