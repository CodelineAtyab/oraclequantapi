#!/bin/bash
# Mac: Docker Oracle XE + DB user + Spring Boot (profile docker)
# Run from: /Users/mac/IdeaProjects/oraclequantapi
set -u

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "${PROJECT_DIR}"

CONTAINER="oracle-xe"

echo "=== [1/4] Oracle XE container ==="
if docker ps -a --format '{{.Names}}' | grep -qx "${CONTAINER}"; then
  echo "Container '${CONTAINER}' already exists — using it (no conflict)."
  docker start "${CONTAINER}" 2>/dev/null || true
else
  echo "Creating new container..."
  docker compose -f docker/docker-compose.yml up -d
fi

if ! docker ps --format '{{.Names}}' | grep -qx "${CONTAINER}"; then
  echo "ERROR: ${CONTAINER} is not running. Start Docker Desktop, then:"
  echo "  cd ~/Desktop/oracle-xe-db-hosting && docker compose up -d"
  exit 1
fi

echo "=== [2/4] Waiting for Oracle ==="
for i in $(seq 1 60); do
  if docker exec "${CONTAINER}" healthcheck.sh >/dev/null 2>&1; then
    echo "Oracle is healthy."
    break
  fi
  if nc -z localhost 1521 2>/dev/null; then
    echo "Port 1521 is open."
    sleep 15
    break
  fi
  echo "  waiting... ($i/60)"
  sleep 5
done

echo "=== [3/4] Creating pkc_user + MEASUREMENT_HISTORY (SYSDBA — no SYSTEM password) ==="
bash "${PROJECT_DIR}/scripts/docker-setup-db-only.sh" || exit 1

echo "=== [4/4] Starting Spring Boot (profile: docker) ==="
echo "  API:         http://localhost:8081/convert-measurements?input=aa"
echo "  DbVisualizer: jdbc:oracle:thin:@//localhost:1521/XEPDB1"
echo "  User:        pkc_user / PkcPassword1"
echo ""

mvn spring-boot:run -Dspring-boot.run.profiles=docker
