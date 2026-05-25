#!/bin/bash
# Run on Oracle Linux VM (same machine as Oracle XE)
set -e
cd "$(dirname "$0")/.."

export SPRING_PROFILES_ACTIVE=oracle
export ORACLE_JDBC_URL="${ORACLE_JDBC_URL:-jdbc:oracle:thin:@//localhost:1521/XEPDB1}"
export ORACLE_USER="${ORACLE_USER:-pkc_user}"
export ORACLE_PASSWORD="${ORACLE_PASSWORD:-PkcPassword1}"

JAR="${1:-./pkc-api.jar}"
if [[ ! -f "$JAR" ]]; then
  echo "Missing JAR: $JAR"
  echo "Usage: ./scripts/start-pkc-api-oracle-linux.sh /path/to/pkc-api.jar"
  exit 1
fi

echo "Starting pkc-api with Oracle XE..."
echo "  URL:  $ORACLE_JDBC_URL"
echo "  User: $ORACLE_USER"
nohup java -Dspring.profiles.active=oracle \
  -jar "$JAR" > pkc-api.out 2>&1 &
echo "PID: $!"
echo "Log: tail -f pkc-api.out"
echo "API: http://$(hostname -I | awk '{print $1}'):8081/convert-measurements?input=aa"
