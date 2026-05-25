# OracleQuant Package Measurement Conversion API

Spring Boot REST API for converting package measurement strings into package totals and storing request history.

## Requirements

- Oracle OpenJDK 17
- Maven 3.9+ or the included Maven wrapper
- Docker if running Oracle XE locally in a container
- Oracle XE Database for Oracle profile usage
- Oracle Linux host for final deployment

## Build The Application

Windows PowerShell:

```powershell
.\mvnw.cmd clean package
```

Linux or macOS:

```bash
./mvnw clean package
```

The runnable jar is created at:

```text
target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

## Running The Application

The default configuration uses an in-memory H2 database. This is useful for local development and testing because Oracle XE is not required.

Run with Maven on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Run the packaged jar on Windows:

```powershell
java -jar target\oraclequantapi-0.0.1-SNAPSHOT.jar
```

Run with Maven on Linux:

```bash
./mvnw spring-boot:run
```

Run the packaged jar on Linux:

```bash
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

The API starts at:

```text
http://localhost:8080
```

The dashboard is available at:

```text
http://localhost:8080/
```

Run tests:

```powershell
.\mvnw.cmd test
```

## Dashboard

The application includes a built-in dashboard served by Spring Boot from:

```text
src/main/resources/static
```

Dashboard files:

```text
src/main/resources/static/index.html
src/main/resources/static/styles.css
src/main/resources/static/app.js
```

Dashboard features:

- Convert package measurement strings from the browser.
- Show conversion results.
- Load request history from the active database.
- Refresh request history.
- Delete one history record.
- Delete all history records.
- Show a clear warning if the Oracle `/history` endpoint fails because of database user or table schema problems.

The dashboard uses the same REST endpoints documented below.

## Configuring The Database

The application supports two database modes.

Default local mode:

- Uses H2 in-memory database.
- Does not require external database setup.
- Configured in `src/main/resources/application.properties`.
- Good for development and tests.

Oracle XE mode:

- Uses Oracle XE through the `oracle` Spring profile.
- Configured in `src/main/resources/application-oracle.properties`.
- Activated with `SPRING_PROFILES_ACTIVE=oracle`.
- Uses environment variables for database URL, username, and password.
- The dashboard works with Oracle as long as `/history` returns a successful JSON response.

### Oracle XE With Docker

Start Oracle XE in Docker:

```powershell
docker run -d --name oracle-xe `
  -p 1521:1521 `
  -e ORACLE_PASSWORD=oracle `
  -e APP_USER=oraclequantapi `
  -e APP_USER_PASSWORD=oraclequantapi `
  -v oracle-xe-data:/opt/oracle/oradata `
  gvenzl/oracle-xe:21-slim
```

Wait until Oracle is ready:

```powershell
docker logs -f oracle-xe
```

Wait for this message:

```text
DATABASE IS READY TO USE!
```

Set Oracle profile variables on Windows PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="oracle"
$env:ORACLE_DB_URL="jdbc:oracle:thin:@//localhost:1521/XEPDB1"
$env:ORACLE_DB_USERNAME="oraclequantapi"
$env:ORACLE_DB_PASSWORD="oraclequantapi"
```

Run the application:

```powershell
.\mvnw.cmd spring-boot:run
```

Useful Docker commands:

```powershell
docker ps
docker stop oracle-xe
docker start oracle-xe
```

### Oracle XE Without Docker

If Oracle XE is installed directly on the machine, create the application user in Oracle:

```sql
CREATE USER oraclequantapi IDENTIFIED BY "oraclequantapi";
GRANT CREATE SESSION TO oraclequantapi;
GRANT CREATE TABLE TO oraclequantapi;
GRANT CREATE SEQUENCE TO oraclequantapi;
ALTER USER oraclequantapi QUOTA UNLIMITED ON USERS;
```

Use this JDBC URL for a common Oracle XE installation:

```text
jdbc:oracle:thin:@//localhost:1521/XEPDB1
```

Set Oracle profile variables on Linux:

```bash
export SPRING_PROFILES_ACTIVE=oracle
export ORACLE_DB_URL='jdbc:oracle:thin:@//localhost:1521/XEPDB1'
export ORACLE_DB_USERNAME='oraclequantapi'
export ORACLE_DB_PASSWORD='oraclequantapi'
```

The application uses Hibernate `ddl-auto=update` by default for this assignment. Override it if needed:

```bash
export ORACLE_DDL_AUTO=validate
```

If the dashboard shows a history warning, test the history endpoint directly:

```text
http://localhost:8080/history
```

If `/history` returns `500`, verify that the Oracle user used by the app owns a compatible `CONVERSION_HISTORY` table.

Expected table columns:

```text
ID
CREATED_AT
SOURCE_IP_ADDRESS
INPUT
OUTPUT
```

If you do not need old history records, the simplest repair is to drop the old table and restart the app so Hibernate recreates it:

```sql
DROP TABLE CONVERSION_HISTORY;
```

## REST API Endpoints

### Convert Measurements

Endpoint:

```http
GET /convert-measurements?input={measurement-string}
```

Encoding rules:

- `_` means `0`.
- `a` means `1`, `b` means `2`, through `z` meaning `26`.
- Values above `26` are encoded by adding characters together.
- A multi-character value starts with one or more `z` characters and ends at the first following non-`z` character.
- Each package starts with a count value, followed by that many measured values.
- Invalid characters are treated as `0`.
- Missing measured values are treated as `0`.
- Empty or missing `input` returns an empty list.

Examples:

```bash
curl 'http://localhost:8080/convert-measurements?input=aa'
# [1]

curl 'http://localhost:8080/convert-measurements?input=abbcc'
# [2,6]

curl 'http://localhost:8080/convert-measurements?input=dz_a_aazzaaa'
# [28,53,1]

curl 'http://localhost:8080/convert-measurements?input=a_'
# [0]

curl 'http://localhost:8080/convert-measurements?input=abcdabcdab'
# [2,7,7]

curl 'http://localhost:8080/convert-measurements?input=abcdabcdab_'
# [2,7,7,0]

curl 'http://localhost:8080/convert-measurements?input=caa'
# [2]

curl 'http://localhost:8080/convert-measurements?input=a1'
# [0]
```

### History Endpoints

History records include:

- `id`
- `timestamp`
- `source_ip_address`
- `input`
- `output`

Available endpoints:

```http
GET /history
GET /history/{id}
PUT /history/{id}
PATCH /history/{id}
DELETE /history
DELETE /history/{id}
```

Get all history records:

```bash
curl 'http://localhost:8080/history'
```

Get one history record:

```bash
curl 'http://localhost:8080/history/1'
```

Patch one history record:

```bash
curl -X PATCH 'http://localhost:8080/history/1' \
  -H 'Content-Type: application/json' \
  -d '{"input":"aa","output":"[1]","source_ip_address":"127.0.0.1"}'
```

Replace or update one history record:

```bash
curl -X PUT 'http://localhost:8080/history/1' \
  -H 'Content-Type: application/json' \
  -d '{"timestamp":"2026-05-25T12:00:00Z","input":"abbcc","output":"[2,6]","source_ip_address":"127.0.0.1"}'
```

Delete one history record:

```bash
curl -X DELETE 'http://localhost:8080/history/1'
```

Delete all history records:

```bash
curl -X DELETE 'http://localhost:8080/history'
```

## Logging

Logging is configured in:

```text
src/main/resources/logback-spring.xml
```

The application writes logs to:

- Console output
- `logs/oraclequantapi.log`

Daily rolling log files are retained for seven days.

Override the log path:

```bash
export LOG_PATH=/var/log/oraclequantapi
```

## Deploy The Jar On Oracle Linux Via SSH

Build the jar locally:

```powershell
.\mvnw.cmd clean package
```

SSH to the Oracle Linux server:

```bash
ssh opc@your-server-ip
```

Create the application directory on the server:

```bash
sudo mkdir -p /opt/oraclequantapi
sudo chown opc:opc /opt/oraclequantapi
```

Exit the server and copy the jar from your local machine:

```bash
scp target/oraclequantapi-0.0.1-SNAPSHOT.jar opc@your-server-ip:/opt/oraclequantapi/oraclequantapi.jar
```

SSH back to the server:

```bash
ssh opc@your-server-ip
```

Install Oracle OpenJDK 17 if needed:

```bash
sudo dnf install -y jdk-17
java -version
```

Create a runtime environment file:

```bash
sudo tee /etc/oraclequantapi.env >/dev/null <<'EOF'
SPRING_PROFILES_ACTIVE=oracle
ORACLE_DB_URL=jdbc:oracle:thin:@//localhost:1521/XEPDB1
ORACLE_DB_USERNAME=oraclequantapi
ORACLE_DB_PASSWORD=oraclequantapi
LOG_PATH=/var/log/oraclequantapi
EOF
```

Create the log directory:

```bash
sudo mkdir -p /var/log/oraclequantapi
sudo chown opc:opc /var/log/oraclequantapi
```

Run the jar manually:

```bash
set -a
. /etc/oraclequantapi.env
set +a
java -jar /opt/oraclequantapi/oraclequantapi.jar
```

Test from another terminal or browser:

```text
http://your-server-ip:8080/convert-measurements?input=abbcc
```

Optional systemd service file:

```ini
[Unit]
Description=OracleQuant API
After=network.target

[Service]
EnvironmentFile=/etc/oraclequantapi.env
ExecStart=/usr/bin/java -jar /opt/oraclequantapi/oraclequantapi.jar
Restart=always
User=opc

[Install]
WantedBy=multi-user.target
```

Save the service file as:

```text
/etc/systemd/system/oraclequantapi.service
```

Enable and start the service:

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now oraclequantapi
sudo systemctl status oraclequantapi
```

View application logs:

```bash
journalctl -u oraclequantapi -f
tail -f /var/log/oraclequantapi/oraclequantapi.log
```

## Project Files

Main application class:

```text
src/main/java/com/oraclequantapi/oraclequantapi/OracleQuantApiApplication.java
```

Main packages:

```text
controller
service
model
repository
```

Important configuration files:

```text
src/main/resources/application.properties
src/main/resources/application-oracle.properties
src/main/resources/logback-spring.xml
```
