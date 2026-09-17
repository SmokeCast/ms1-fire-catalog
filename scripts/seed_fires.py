import os
import requests
import pandas as pd
import mysql.connector
from dotenv import load_dotenv

# Carga las variables desde el archivo .env
load_dotenv()

MAP_KEY = os.getenv("FIRMS_MAP_KEY")
DB_HOST = os.getenv("DB_HOST", "localhost")
DB_USER = os.getenv("DB_USER", "root")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_NAME = os.getenv("DB_NAME", "fire_catalog")

BBOX = "-81,-18,-68,0"
DAYS = 5

url = f"https://firms.modaps.eosdis.nasa.gov/api/area/csv/{MAP_KEY}/VIIRS_NOAA20_NRT/{BBOX}/{DAYS}"

print("Descargando datos de NASA FIRMS...")
df = pd.read_csv(url)
print(f"Se descargaron {len(df)} detecciones")

conn = mysql.connector.connect(
    host="localhost", user="root", password="smokecast123", database="fire_catalog"
)
cursor = conn.cursor()

# 1. Crear un evento genérico en fire_events
cursor.execute(
    """
    INSERT INTO fire_events (centroid_lat, centroid_lon, max_frp, detection_count, first_detected_at, last_detected_at) 
    VALUES (%s, %s, %s, %s, NOW(), NOW())
""",
    (
        float(df["latitude"].mean()),
        float(df["longitude"].mean()),
        float(df["frp"].max()),
        len(df),
    ),
)

event_id = cursor.lastrowid

# 2. Preparar e insertar masivamente en fire_detections
rows = []
for _, r in df.iterrows():
    acq_time_formatted = str(r["acq_time"]).zfill(4)
    confidence_val = str(r["confidence"]) if pd.notna(r["confidence"]) else None

    rows.append(
        (
            event_id,
            float(r["latitude"]),
            float(r["longitude"]),
            float(r["bright_ti4"]),
            float(r["frp"]),
            confidence_val,
            str(r["acq_date"]),
            acq_time_formatted,
        )
    )

cursor.executemany(
    """
    INSERT INTO fire_detections (fire_event_id, latitude, longitude, brightness, frp, confidence, acq_date, acq_time) 
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
""",
    rows,
)

conn.commit()
print(
    f"¡Listo! {len(rows)} detecciones insertadas asociadas al fire_event_id: {event_id}"
)

cursor.close()
conn.close()
