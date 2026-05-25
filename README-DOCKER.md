# Docker Setup for OracleQuant PKC API

Run the entire project with a single command using Docker.

## Prerequisites

- Docker Desktop installed and running
- Java 17 and Maven (only for the initial JAR build)

## How to Build and Run with Docker

```bash
# 1. Build the JAR file
mvn clean package -DskipTests

# 2. Build Docker images and start containers
docker-compose up --build
```

The application will be available at `http://localhost:8080`.

> **Note:** Oracle XE takes 2–3 minutes to start for the first time. The `pkc-api` service waits for the database health check to pass before starting.

## How to Stop

```bash
docker-compose down
```

To also remove the persisted database volume:

```bash
docker-compose down -v
```

## How to Test

All endpoints are available at `http://localhost:8080`.

### Convert Measurements

```bash
curl "http://localhost:8080/convert-measurements?input=abbcc"
```

Response:
```json
{"packages": [2, 6]}
```

### Get All History Records

```bash
curl "http://localhost:8080/history"
```

### Get Single History Record

```bash
curl "http://localhost:8080/history/1"
```

### Update History Record

```bash
curl -X PUT "http://localhost:8080/history/1" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa","output":"[1]","sourceIpAddress":"10.0.0.1","timestamp":"2026-05-21T10:00:00"}'
```

### Partial Update History Record

```bash
curl -X PATCH "http://localhost:8080/history/1" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa"}'
```

### Delete All History Records

```bash
curl -X DELETE "http://localhost:8080/history"
```

### View Container Logs

```bash
docker-compose logs -f pkc-api
docker-compose logs -f oracle-db
```
