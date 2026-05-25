#!/bin/bash
# Mac: Oracle (existing container) + Spring Boot in Docker + DbVisualizer
set -u
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "${PROJECT_DIR}"
NETWORK="pkc-net"
CONTAINER_ORACLE="oracle-xe"
CONTAINER_API="pkc-api"

echo "=== [1/6] Stop old API ==="
bash "${PROJECT_DIR}/scripts/stop-pkc-api.sh" 2>/dev/null || true
docker rm -f "${CONTAINER_API}" 2>/dev/null || true

echo "=== [2/6] Build JAR ==="
mvn -q clean package -DskipTests
test -f target/pkc-api.jar || { echo "Missing target/pkc-api.jar"; exit 1; }

echo "=== [3/6] Start Oracle container ==="
if ! docker ps --format '{{.Names}}' | grep -qx "${CONTAINER_ORACLE}"; then
  if docker ps -a --format '{{.Names}}' | grep -qx "${CONTAINER_ORACLE}"; then
    docker start "${CONTAINER_ORACLE}"
  else
    echo "Start Oracle first:"
    echo "  cd ~/Desktop/oracle-xe-db-hosting && docker compose up -d"
    exit 1
  fi
fi

echo "=== [4/6] Docker network ${NETWORK} (so API can reach oracle-xe by name) ==="
docker network create "${NETWORK}" 2>/dev/null || true
docker network connect "${NETWORK}" "${CONTAINER_ORACLE}" 2>/dev/null || true

echo "=== [5/6] Wait for Oracle + setup pkc_user ==="
for i in $(seq 1 40); do
  if docker exec "${CONTAINER_ORACLE}" healthcheck.sh >/dev/null 2>&1; then
    echo "Oracle healthy."
    break
  fi
  sleep 5
done
bash "${PROJECT_DIR}/scripts/docker-setup-db-only.sh"

echo "=== [6/6] Start API container on ${NETWORK} ==="
docker run -d --name "${CONTAINER_API}" \
  --network "${NETWORK}" \
  -p 8081:8081 \
  -v "${PROJECT_DIR}/target/pkc-api.jar:/app/pkc-api.jar:ro" \
  -e SPRING_PROFILES_ACTIVE=docker-internal \
  eclipse-temurin:17-jre \
  java -jar /app/pkc-api.jar

echo "Waiting for API..."
for i in $(seq 1 40); do
  if curl -sf "http://127.0.0.1:8081/convert-measurements?input=aa" >/dev/null 2>&1; then
    echo ""
    echo "=============================================="
    echo " READY"
    echo " Postman:      http://127.0.0.1:8081/convert-measurements?input=aa"
    echo " DbVisualizer: jdbc:oracle:thin:@//127.0.0.1:1521/XEPDB1"
    echo "               pkc_user / PkcPassword1"
    echo " Table:        PKC_USER.MEASUREMENT_HISTORY"
    echo " API logs:     docker logs -f pkc-api"
    echo "=============================================="
    exit 0
  fi
  sleep 3
done

echo "API not ready. Logs:"
docker logs "${CONTAINER_API}" --tail 60
exit 1
