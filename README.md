# OracleQuant PKC API

## Project Overview

The OracleQuant PKC (Package Measurement Conversion) API converts encoded letter sequences into integer package measurements. Given a string of characters `a-z` and underscores, it parses packages using a count-value encoding scheme and returns a JSON object containing an array of integers. The API also maintains a full history of conversion requests with CRUD endpoints for record management.

Built with Java 17, Spring Boot 3.5, Oracle XE 21c, and Maven.

## Prerequisites

- Oracle OpenJDK 17
- Maven 3.8+
- Oracle XE 21c (with `pkc_user` created)
- Oracle Linux (for deployment)

---

## Running Tests

Run the unit tests (no database required):

```bash
# All tests
mvn test

# Single test class
mvn test -Dtest=SequenceServiceTest

# Single test method
mvn test -Dtest=SequenceServiceTest#testAa
```

### Test Coverage

| Test | Input | Expected |
|------|-------|----------|
| `testAa` | `aa` | `[1]` |
| `testAbbcc` | `abbcc` | `[2, 6]` |
| `testDz_a_aazzaaa` | `dz_a_aazzaaa` | `[28, 1]` |
| `testA_` | `a_` | `[0]` |
| `testAbcdabcdab` | `abcdabcdab` | `[2, 7, 7]` |
| `testAbcdabcdab_` | `abcdabcdab_` | `[2, 7, 7, 0]` |
| `testZdaaaaaaaabaaaaaaaabaaaaaaaabbaa` | `zdaaaaaaaabaaaaaaaabaaaaaaaabbaa` | `[34]` |
| `testZa_a_a_a_a_a_a_a_a_a_a_a_a_azaaa` | `za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa` | `[40, 1]` |

---

## How to Build and Run Locally

```bash
# Clone the repository
git clone <repo-url>
cd oraclequantapi

# Build the JAR (requires Oracle XE for full build)
mvn clean package

# Build the JAR and skip tests (no database needed)
mvn clean package -DskipTests

# Run the application
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

The application starts on port `8080` by default.

---

## Database Configuration

### Create Database User

Connect to your Oracle XE database and run:

```sql
ALTER SESSION SET CONTAINER=XEPDB1;
CREATE USER pkc_user IDENTIFIED BY pkc_password;
GRANT CONNECT, RESOURCE TO pkc_user;
GRANT UNLIMITED TABLESPACE TO pkc_user;
GRANT CREATE SEQUENCE TO pkc_user;
GRANT CREATE TABLE TO pkc_user;
```

### Application Properties

Configured in `src/main/resources/application.properties`:

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

Hibernate `ddl-auto=update` creates the `conversion_history` table automatically on startup.

---

## REST API Endpoints

### Convert Measurements

```
GET /convert-measurements?input=abbcc
```

```bash
curl "http://localhost:8080/convert-measurements?input=abbcc"
```

Response (200 OK):
```json
{"packages": [2, 6]}
```

#### Input Validation

- `input` parameter is required and cannot be blank.
- Only lowercase letters (`a-z`) and underscores (`_`) are allowed.
- Invalid input returns `400 Bad Request`:
```json
{"error": "Invalid input: ..."}
```

#### Auto-Evaluation Reference Table

| Request (`?input=...`) | Response (`{"packages": [...]}`) |
|------------------------|----------------------------------|
| `aa` | `[1]` |
| `abbcc` | `[2, 6]` |
| `dz_a_aazzaaa` | `[28, 1]` |
| `a_` | `[0]` |
| `abcdabcdab` | `[2, 7, 7]` |
| `abcdabcdab_` | `[2, 7, 7, 0]` |
| `zdaaaaaaaabaaaaaaaabaaaaaaaabbaa` | `[34]` |
| `za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa` | `[40, 1]` |

### Get All History Records

```
GET /history
```

```bash
curl "http://localhost:8080/history"
```

Response (200 OK):
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

Response (200 OK):
```json
{
  "id": 1,
  "timestamp": "2026-05-21T09:30:00",
  "sourceIpAddress": "127.0.0.1",
  "input": "abbcc",
  "output": "[2, 6]"
}
```

Returns `404 Not Found` if the record does not exist.

### Update History Record

```
PUT /history/{id}
```

```bash
curl -X PUT "http://localhost:8080/history/1" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa","output":"[1]","sourceIpAddress":"10.0.0.1","timestamp":"2026-05-21T10:00:00"}'
```

Response: the updated record (200 OK).

### Partial Update History Record

```
PATCH /history/{id}
```

```bash
curl -X PATCH "http://localhost:8080/history/1" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa"}'
```

Response: the partially updated record (200 OK).

### Delete All History Records

```
DELETE /history
```

```bash
curl -X DELETE "http://localhost:8080/history"
```

Response: `204 No Content`.

---

## Postman Testing

1. Create a new **GET** request to `http://localhost:8080/convert-measurements`
2. Add a **Query Param**: `input` = `aa`
3. Send → expect `{"packages": [1]}`
4. All history endpoints are **GET/PUT/PATCH/DELETE** on `http://localhost:8080/history`

### Collection of Test Requests

| Method | URL | Notes |
|--------|-----|-------|
| `GET` | `/convert-measurements?input=aa` | Basic single package |
| `GET` | `/convert-measurements?input=abbcc` | Multi-package |
| `GET` | `/convert-measurements?input=dz_a_aazzaaa` | z in count + value |
| `GET` | `/convert-measurements?input=a_` | Underscore = 0 |
| `GET` | `/history` | Fetch all history |
| `GET` | `/history/1` | Fetch by ID |
| `PUT` | `/history/1` | Full update (JSON body) |
| `PATCH` | `/history/1` | Partial update (JSON body) |
| `DELETE` | `/history` | Clear all history |

---

## Deploy to Oracle Linux via SSH

### Step 1: Build the JAR

```bash
# On your local machine
mvn clean package -DskipTests
```

### Step 2: Copy to VM using SCP

```bash
scp target/oraclequantapi-0.0.1-SNAPSHOT.jar oracle@<vm-ip>:/home/oracle/
```

### Step 3: SSH into VM and Run

```bash
ssh oracle@<vm-ip>

# Verify Java 17
java -version

# Run the application
cd /home/oracle
java -jar oraclequantapi-0.0.1-SNAPSHOT.jar
```

### Step 4: Run as a Background Service

For persistent execution after logout:

```bash
nohup java -jar oraclequantapi-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

# Check startup logs
tail -f app.log

# Find process later
ps aux | grep oraclequantapi

# Stop it
kill <pid>
```

### Step 5: Configure Firewall

```bash
sudo firewall-cmd --add-port=8080/tcp --permanent
sudo firewall-cmd --reload
```

### Step 6: Verify Remotely

```bash
curl "http://<vm-ip>:8080/convert-measurements?input=aa"
```

Expected:
```json
{"packages": [1]}
```

---

## Logging

Logs are written to `logs/pkc-api.log` using a rolling file appender with 7-day retention. Archived logs are named `logs/pkc-api.YYYY-MM-DD.log`. The logging level for the `com.oraclequantapi` package is set to `DEBUG` in `application.properties`.

Console and file output use the format:
```
yyyy-MM-dd HH:mm:ss [thread] LEVEL logger - message
```

---

## Changelog

See [CHANGELOG.md](./CHANGELOG.md) for version history.

## Version

Current version: `0.0.1-SNAPSHOT` (see [version.txt](./version.txt)).
