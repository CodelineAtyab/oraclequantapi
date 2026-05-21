# OracleQuant PKC API

## Project Overview

The OracleQuant PKC (Package Measurement Conversion) API converts encoded letter sequences into integer package measurements. Given a string of characters `a-z` and underscores, it parses packages using a count-value encoding scheme and returns an array of integers. The API also maintains a full history of conversion requests with CRUD endpoints for record management.

Built with Java 17, Spring Boot 3.5, Oracle XE 21c, and Maven.

## Prerequisites

- Oracle OpenJDK 17
- Maven 3.8+
- Oracle XE 21c
- Oracle Linux (for deployment)

## How to Build and Run Locally

```bash
# Clone the repository
git clone <repo-url>
cd oraclequantapi

# Build the JAR
mvn clean package

# Run the application (requires Oracle XE running on localhost:1521)
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

The application starts on port `8080` by default.

## Database Configuration

### Install Oracle XE on Oracle Linux

```bash
# Download Oracle XE 21c RPM and install
sudo dnf install -y oracle-database-xe-21c-1.0-1.ol8.x86_64.rpm

# Configure the database
sudo /etc/init.d/oracle-xe-21c configure

# Set environment variables
export ORACLE_SID=XE
export ORAENV_ASK=NO
source /usr/bin/oraenv
```

### Create Database User

```sql
-- Connect as SYSTEM
sqlplus system@localhost:1521/XEPDB1

-- Create application user
CREATE USER pkc_user IDENTIFIED BY pkc_password;
GRANT CONNECT, RESOURCE TO pkc_user;
GRANT UNLIMITED TABLESPACE TO pkc_user;
GRANT CREATE SEQUENCE TO pkc_user;
GRANT CREATE TABLE TO pkc_user;
```

### Application Properties

Configure `src/main/resources/application.properties`:

```properties
server.port=8080

spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=pkc_user
spring.datasource.password=pkc_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect

logging.level.com.oraclequantapi=DEBUG
```

## REST API Endpoints

### Convert Measurements

```
GET /convert-measurements?input=abbcc
```

```bash
curl "http://localhost:8080/convert-measurements?input=abbcc"
```

Response:
```json
[2, 6]
```

### Get All History Records

```
GET /history
```

```bash
curl "http://localhost:8080/history"
```

Response:
```json
[
  {
    "id": 1,
    "timestamp": "2026-05-21T09:30:00",
    "sourceIpAddress": "127.0.0.1",
    "input": "abbcc",
    "output": "[2, 6]"
  }
]
```

### Get Single History Record

```
GET /history/{id}
```

```bash
curl "http://localhost:8080/history/1"
```

Response:
```json
{
  "id": 1,
  "timestamp": "2026-05-21T09:30:00",
  "sourceIpAddress": "127.0.0.1",
  "input": "abbcc",
  "output": "[2, 6]"
}
```

### Update History Record

```
PUT /history/{id}
```

```bash
curl -X PUT "http://localhost:8080/history/1" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa","output":"[1]","sourceIpAddress":"10.0.0.1","timestamp":"2026-05-21T10:00:00"}'
```

Response: updated record.

### Partial Update History Record

```
PATCH /history/{id}
```

```bash
curl -X PATCH "http://localhost:8080/history/1" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa"}'
```

Response: updated record with only the `input` field changed.

### Delete All History Records

```
DELETE /history
```

```bash
curl -X DELETE "http://localhost:8080/history"
```

Response: `204 No Content`.

## Deploy to Oracle Linux via SSH

### Step 1: Build the JAR on Windows

```bash
mvn clean package
```

### Step 2: Copy to VM using SCP

```bash
scp target/oraclequantapi-0.0.1-SNAPSHOT.jar oracle@<vm-ip>:/home/oracle/
```

### Step 3: SSH into VM and Run

```bash
ssh oracle@<vm-ip>
cd /home/oracle
java -jar oraclequantapi-0.0.1-SNAPSHOT.jar
```

For persistent execution:

```bash
nohup java -jar oraclequantapi-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

### Step 4: Open Firewall Port

```bash
sudo firewall-cmd --add-port=8080/tcp --permanent
sudo firewall-cmd --reload
```

### Step 5: Verify Remotely

```bash
curl "http://<vm-ip>:8080/convert-measurements?input=aa"
```

Expected: `[1]`

## Logging

Logs are written to `logs/pkc-api.log` using a rolling file appender with 7-day retention. The logging level for the `com.oraclequantapi` package is set to `DEBUG` in `application.properties`.

## Changelog

See [CHANGELOG.md](./CHANGELOG.md) for version history.
