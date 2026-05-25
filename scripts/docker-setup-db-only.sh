#!/bin/bash
# Create pkc_user + MEASUREMENT_HISTORY in running oracle-xe container
# Uses SYSDBA inside container (no SYSTEM password needed)
set -u

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
CONTAINER="${ORACLE_CONTAINER:-oracle-xe}"

if ! docker ps --format '{{.Names}}' | grep -q "^${CONTAINER}$"; then
  echo "Container ${CONTAINER} is not running."
  echo "Start it with:"
  echo "  cd ~/Desktop/oracle-xe-db-hosting && docker compose up -d"
  echo "  OR"
  echo "  cd ${PROJECT_DIR} && docker compose -f docker/docker-compose.yml up -d"
  exit 1
fi

echo "Setting up pkc_user via SYSDBA in container ${CONTAINER}..."
docker cp "${PROJECT_DIR}/docker/setup-pkc-user-grants-only.sql" "${CONTAINER}:/tmp/setup-pkc-user.sql"

if docker exec -i "${CONTAINER}" bash -c 'sqlplus -s / as sysdba @/tmp/setup-pkc-user.sql'; then
  echo ""
  echo "DB setup OK."
  echo "  DbVisualizer URL:  jdbc:oracle:thin:@//localhost:1521/XEPDB1"
  echo "  User / Password:   pkc_user / PkcPassword1"
  echo "  Table:             PKC_USER.MEASUREMENT_HISTORY"
else
  echo ""
  echo "DB setup failed. Last lines from container:"
  docker logs "${CONTAINER}" --tail 20
  exit 1
fi
