#!/usr/bin/env bash
# ===================================================================
#  One-shot installer for the MARYAM Measurement Conversion API on
#  Oracle Linux 8 / 9 (or RHEL).
#
#  Prerequisites on the target box:
#    - Oracle XE 21c already installed and listening on 1521
#    - SYS password = 1234 (matches application.properties)
#    - You unzipped the project somewhere and are running this script
#      from inside the project root.
#
#  What it does:
#    1. Installs OpenJDK 17 and Maven if missing.
#    2. Creates the maryam OS user, /opt and /var/log layout.
#    3. Builds the jar with `mvn clean package`.
#    4. Creates the MARYAM_APP database user and schema.
#    5. Installs and starts the systemd service.
#    6. Runs the eight PDF smoke tests.
# ===================================================================
set -euo pipefail

APP_USER=maryam
APP_DIR=/opt/maryam-measurement-api
LOG_DIR=/var/log/maryam-measurement-api
SERVICE=maryam-measurement-api
PROJECT_ROOT="$(cd "$(dirname "$0")/.." && pwd)"

echo ">> 1/6  Installing OpenJDK 17 and Maven"
sudo dnf install -y java-17-openjdk java-17-openjdk-devel maven >/dev/null

echo ">> 2/6  Creating OS user and filesystem layout"
id "$APP_USER" >/dev/null 2>&1 || sudo useradd -m -s /bin/bash "$APP_USER"
sudo mkdir -p "$APP_DIR" "$LOG_DIR"
sudo chown -R "$APP_USER":"$APP_USER" "$APP_DIR" "$LOG_DIR"

echo ">> 3/6  Building the jar"
cd "$PROJECT_ROOT"
mvn -q clean package -DskipTests

echo ">> 4/6  Provisioning the database (MARYAM_APP user)"
if command -v sqlplus >/dev/null 2>&1; then
    sqlplus -L sys/1234@//localhost:1521/XEPDB1 as sysdba \
        @deployment/oracle_xe_setup.sql || \
        echo "   (setup script reported an issue, continuing - check manually)"
else
    echo "   sqlplus not on PATH; skipping schema provisioning."
    echo "   Run it manually:  sqlplus sys/1234@//localhost:1521/XEPDB1 as sysdba @deployment/oracle_xe_setup.sql"
fi

echo ">> 5/6  Installing the systemd unit"
sudo cp target/maryam-measurement-api.jar              "$APP_DIR/"
sudo cp src/main/resources/application.properties    "$APP_DIR/"
sudo cp deployment/maryam-measurement-api.service      "/etc/systemd/system/"
sudo chown -R "$APP_USER":"$APP_USER" "$APP_DIR"

sudo systemctl daemon-reload
sudo systemctl enable "$SERVICE" >/dev/null
sudo systemctl restart "$SERVICE"
sleep 5
sudo systemctl --no-pager status "$SERVICE" | head -n 10

echo ""
echo ">> 6/6  Smoke testing the eight PDF examples"
for pair in \
    "aa|[1]" \
    "abbcc|[2,6]" \
    "dz_a_aazzaaa|[28,53,1]" \
    "a_|[0]" \
    "abcdabcdab|[2,7,7]" \
    "abcdabcdab_|[2,7,7,0]" \
    "zdaaaaaaaabaaaaaaaabaaaaaaaabbaa|[34]" \
    "za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa|[40,1]"
do
    IN="${pair%%|*}"
    EXP="${pair##*|}"
    GOT=$(curl -s "http://localhost:8080/maryam/convert-measurements?input=$IN")
    if [ "$GOT" = "$EXP" ]; then
        echo "   OK   $IN -> $GOT"
    else
        echo "   FAIL $IN expected $EXP got $GOT"
    fi
done

echo ""
echo "Install complete.  Try:  curl http://localhost:8080/maryam/history"
echo "Service logs:           sudo journalctl -u $SERVICE -f"
