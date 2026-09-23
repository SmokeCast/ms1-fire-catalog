# MS1 — Fire Catalog

Java 21, Spring Boot 4.1.1 y MySQL 8.0. Maven Wrapper 3.9.16 incluido.
La aplicación está directamente en esta carpeta (ya no en `smokecast/`).

## Ejecución local

Instalar un JDK 21 y tener MySQL accesible. Crear la base vacía con un cliente SQL:

```sql
CREATE DATABASE IF NOT EXISTS fire_catalog;
```

Desde esta carpeta:

```sh
cp env.example .env
# Configurar DB_URL, DB_USERNAME y DB_PASSWORD.
./mvnw spring-boot:run
```

Windows: `mvnw.cmd spring-boot:run`. Spring carga `.env` como un archivo properties:
usar `CLAVE=valor` sin `export` ni comillas de shell. Las variables de entorno tienen prioridad.
Puerto predeterminado: 8081. Hibernate crea/actualiza las tablas `fire_events` y
`fire_detections`; se mantiene `ddl-auto=update` para desarrollo local.

Documentación OpenAPI/Swagger: `http://127.0.0.1:8081/docs` · JSON: `/openapi.json`.
No ejecuta seeds. Si la base está vacía, los listados devuelven páginas vacías.

## Endpoints

- `GET /health`: verifica conexión SQL.
- `GET /api/v1/fires?page=0&size=10`: página de eventos.
- `GET /api/v1/fires/{id}`: detalle del evento.
- `GET /api/v1/detections?page=0&size=10`: página de detecciones.
- `GET /api/v1/detections/{id}`: detalle de detección.
- `POST /api/v1/fires/bulk`: inserta hasta 10 000 eventos y devuelve sus IDs.
- `POST /api/v1/detections/bulk`: inserta hasta 10 000 detecciones asociadas a eventos existentes.

Los listados conservan el objeto paginado con `content`; no son arrays directos.
Un ID inexistente devuelve 404. Paginación inválida devuelve 400; `size` máximo 500.
Los DTOs y ModelMapper evitan exponer directamente las entidades JPA. Los
endpoints bulk están pensados para cargar lotes de la ingesta, no para sustituir
el seed inicial.

## Verificación

`./mvnw test` usa H2 únicamente para pruebas, sin necesitar MySQL.
`./mvnw package` genera el JAR en `target/`.

El listado acepta `country`, `severity` (`Bajo`, `Moderado`, `Alto`, `Crítico`) y
`q` (país, ID exacto o `Incendio #123`). Los filtros se combinan antes de paginar;
`totalElements` y `totalPages` representan las coincidencias. Orden estable por ID.
Ejemplo: `/api/v1/fires?country=Chile&severity=Bajo&page=0&size=100`.
`GET /api/v1/fires/countries` devuelve los países de todo el catálogo para el selector.
