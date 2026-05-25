#!/bin/bash
# Stop anything using port 8081 and old pkc-api / spring-boot runs
echo "Stopping processes on port 8081..."
for pid in $(lsof -ti :8081 2>/dev/null); do
  echo "  kill PID $pid"
  kill "$pid" 2>/dev/null || kill -9 "$pid" 2>/dev/null
done

pkill -f "oraclequantapi.*spring-boot:run" 2>/dev/null || true
pkill -f "pkc-api.jar" 2>/dev/null || true
pkill -f "OraclequantapiApplication" 2>/dev/null || true
docker stop pkc-api 2>/dev/null || true

sleep 2
if lsof -i :8081 >/dev/null 2>&1; then
  echo "Port 8081 still in use — force kill:"
  lsof -ti :8081 | xargs kill -9 2>/dev/null || true
fi

if lsof -i :8081 >/dev/null 2>&1; then
  echo "ERROR: port 8081 still busy"
  lsof -i :8081
  exit 1
fi

echo "Port 8081 is free. You can start Spring Boot now."
