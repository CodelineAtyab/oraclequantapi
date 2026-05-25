#!/bin/bash
# =============================================================================
# Run on your MAC — builds JAR and copies to Oracle Linux VM
# Usage:  bash scripts/mac-send-jar-to-linux.sh
#         bash scripts/mac-send-jar-to-linux.sh alharith@127.0.0.1
# =============================================================================

set -e
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "${PROJECT_DIR}"

# Default: SSH via VirtualBox port-forward (set in VM Network -> Port Forwarding: 2222 -> 22)
VM_SSH_TARGET="${1:-alharith@127.0.0.1}"
VM_SSH_PORT="${VM_SSH_PORT:-2222}"

echo "Building pkc-api.jar..."
mvn -q clean package -DskipTests

echo "Copying to Linux (${VM_SSH_TARGET})..."
scp -P "${VM_SSH_PORT}" "${PROJECT_DIR}/target/pkc-api.jar" "${VM_SSH_TARGET}:/home/alharith/pkc-api.jar"

echo "Copying setup script..."
scp -P "${VM_SSH_PORT}" "${PROJECT_DIR}/scripts/oracle-linux-all-in-one.sh" "${VM_SSH_TARGET}:/home/alharith/"

echo ""
echo "Done. Now SSH to Linux and run:"
echo "  ssh -p ${VM_SSH_PORT} alharith@127.0.0.1"
echo "  bash /home/alharith/oracle-linux-all-in-one.sh"
