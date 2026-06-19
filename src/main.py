"""
Scala-PDF-Generator: Production microservice: Scala PDF Generator
"""
import time
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI(title="Scala-PDF-Generator", version="3.0.0")

class DataPoint(BaseModel):
    metric: str
    value: float
    threshold: float

@app.post("/api/v1/analyze")
def analyze(point: DataPoint):
    is_anomaly = abs(point.value) > point.threshold
    deviation = abs(point.value - point.threshold)
    return {
        "metric": point.metric,
        "is_anomaly": is_anomaly,
        "deviation": round(deviation, 4),
        "severity": "high" if deviation > point.threshold * 0.5 else "low"
    }


@app.get("/health")
def health():
    return {"status": "healthy", "service": "Scala-PDF-Generator", "timestamp": int(time.time())}
