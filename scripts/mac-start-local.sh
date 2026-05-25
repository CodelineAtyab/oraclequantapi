#!/bin/bash
# Start pkc-api on Mac with persistent H2 (DbVisualizer can connect)
cd "$(dirname "$0")/.."
mvn -q spring-boot:run -Dspring-boot.run.profiles=local
