# OracleQuant ERP - Package Measurement Conversion API

Spring Boot API for converting encoded package measurement strings into total measured inflows per package.

## Requirements

- Oracle OpenJDK 17
- Maven wrapper included in this repository
- Oracle XE Database for production/runtime persistence

## Build And Test

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

The runnable artifact is created at:

```text
target/pkc-api.jar
```

## Oracle XE Configuration

The application reads database settings from environment variables:

```bash
export PKC_DB_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1"
export PKC_DB_USERNAME="pkc_user"
export PKC_DB_PASSWORD="pkc_password"
export PKC_JPA_DDL_AUTO="update"
```

Create the Oracle user before running the application:

```sql
CREATE USER pkc_user IDENTIFIED BY pkc_password;
GRANT CONNECT, RESOURCE TO pkc_user;
ALTER USER pkc_user QUOTA UNLIMITED ON USERS;
```

## Run Locally

For quick local Postman testing without Oracle XE, run the application with the `local` profile. This uses an in-memory H2 database:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

Or run the packaged jar with the local profile:

```powershell
.\mvnw.cmd package
java -jar target/pkc-api.jar --spring.profiles.active=local
```

Use the default profile when Oracle XE is running and you want history persisted in Oracle:

```powershell
$env:PKC_DB_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1"
$env:PKC_DB_USERNAME="pkc_user"
$env:PKC_DB_PASSWORD="pkc_password"
.\mvnw.cmd spring-boot:run
```

Or run the packaged jar with Oracle:

```bash
java -jar target/pkc-api.jar
```

The API listens on `http://localhost:8080` by default. Override with `SERVER_PORT`.

## Conversion API

`GET /convert-measurements?input={encoded-input}`

Examples:

```bash
curl "http://localhost:8080/convert-measurements?input=aa"
# [1]

curl "http://localhost:8080/convert-measurements?input=abbcc"
# [2,6]

curl "http://localhost:8080/convert-measurements?input=dz_a_aazzaaa"
# [28,53,1]
```

Encoding rules:

- `a` through `z` represent `1` through `26`.
- Numbers higher than `26` start with one or more `z` characters and end at the first non-`z` character.
- Non-letter characters such as `_` contribute `0` and can terminate a multi-character number.
- Each package starts with a count, followed by that many measured values. The response contains the sum for each package.

## Postman Test Cases

Start the application first. If Oracle XE is not running locally, use:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

Create a Postman environment variable:

```text
base_url = http://localhost:8080
```

Before testing history, clear old records:

```text
DELETE {{base_url}}/history
```

### Conversion Tests

For each row, create a `GET` request and verify the response body exactly matches the expected JSON array.

| Case | Method | URL | Expected status | Expected body |
| --- | --- | --- | --- | --- |
| 1 | GET | `{{base_url}}/convert-measurements?input=aa` | `200 OK` | `[1]` |
| 2 | GET | `{{base_url}}/convert-measurements?input=abbcc` | `200 OK` | `[2,6]` |
| 3 | GET | `{{base_url}}/convert-measurements?input=dz_a_aazzaaa` | `200 OK` | `[28,53,1]` |
| 4 | GET | `{{base_url}}/convert-measurements?input=a_` | `200 OK` | `[0]` |
| 5 | GET | `{{base_url}}/convert-measurements?input=abcdabcdab` | `200 OK` | `[2,7,7]` |
| 6 | GET | `{{base_url}}/convert-measurements?input=abcdabcdab_` | `200 OK` | `[2,7,7,0]` |
| 7 | GET | `{{base_url}}/convert-measurements?input=zdaaaaaaaabaaaaaaaabaaaaaaaabbaa` | `200 OK` | `[34]` |
| 8 | GET | `{{base_url}}/convert-measurements?input=za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa` | `200 OK` | `[40,1]` |

Use this Postman `Tests` script for each conversion request. Change `expected` for each case:

```javascript
const expected = [1];

pm.test("status is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("response is expected JSON array", function () {
    pm.expect(pm.response.json()).to.eql(expected);
});
```

Example for case 3:

```javascript
const expected = [28, 53, 1];

pm.test("status is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("response is expected JSON array", function () {
    pm.expect(pm.response.json()).to.eql(expected);
});
```

### History Tests In Postman

After running any conversion request, the API saves a history record.

#### Get All History

```text
GET {{base_url}}/history
```

Expected:

```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "timestamp": "2026-05-20T11:00:00.000000",
    "input": "aa",
    "output": "[1]"
  }
]
```

Postman `Tests` script:

```javascript
pm.test("status is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("history contains required fields", function () {
    const records = pm.response.json();
    pm.expect(records.length).to.be.above(0);

    const record = records[0];
    pm.expect(record).to.have.property("id");
    pm.expect(record).to.have.property("timestamp");
    pm.expect(record).to.have.property("input");
    pm.expect(record).to.have.property("output");

    pm.collectionVariables.set("history_id", record.id);
});
```

#### Get One History Record

```text
GET {{base_url}}/history/{{history_id}}
```

Postman `Tests` script:

```javascript
pm.test("status is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("history record id matches", function () {
    const record = pm.response.json();
    pm.expect(record.id).to.eql(pm.collectionVariables.get("history_id"));
});
```

#### Update History Record

```text
PATCH {{base_url}}/history/{{history_id}}
Content-Type: application/json
```

Body:

```json
{
  "input": "a_",
  "output": "[0]"
}
```

Postman `Tests` script:

```javascript
pm.test("status is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("history record was updated", function () {
    const record = pm.response.json();
    pm.expect(record.input).to.eql("a_");
    pm.expect(record.output).to.eql("[0]");
});
```

#### Clear History

```text
DELETE {{base_url}}/history
```

Postman `Tests` script:

```javascript
pm.test("status is 200", function () {
    pm.response.to.have.status(200);
});
```

Then confirm history is empty:

```text
GET {{base_url}}/history
```

Expected body:

```json
[]
```

## History API

History records contain `id`, `timestamp`, `input`, and `output`.

```bash
curl "http://localhost:8080/history"
curl "http://localhost:8080/history/550e8400-e29b-41d4-a716-446655440000"

curl -X PATCH "http://localhost:8080/history/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{"input":"a_","output":"[0]"}'

curl -X PUT "http://localhost:8080/history/550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{"input":"aa","output":"[1]"}'

curl -X DELETE "http://localhost:8080/history"
```

## Logging

Logs are written to the console and to rolling files under `logs/`.
The application keeps seven days of compressed log files.

## Deploy On Oracle Linux Via SSH

Build the jar locally:

```powershell
.\mvnw.cmd package
```

Copy it to the Oracle Linux host:

```bash
scp target/pkc-api.jar opc@YOUR_HOST:/home/opc/pkc-api.jar
```

SSH into the host and run it:

```bash
ssh opc@YOUR_HOST
export PKC_DB_URL="jdbc:oracle:thin:@localhost:1521/XEPDB1"
export PKC_DB_USERNAME="pkc_user"
export PKC_DB_PASSWORD="pkc_password"
java -jar /home/opc/pkc-api.jar
```

For a long-running service, create `/etc/systemd/system/pkc-api.service`:

```ini
[Unit]
Description=OracleQuant Package Measurement Conversion API
After=network.target

[Service]
User=opc
Environment=PKC_DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
Environment=PKC_DB_USERNAME=pkc_user
Environment=PKC_DB_PASSWORD=pkc_password
ExecStart=/usr/bin/java -jar /home/opc/pkc-api.jar
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

Enable and start:

```bash
sudo systemctl daemon-reload
sudo systemctl enable pkc-api
sudo systemctl start pkc-api
sudo systemctl status pkc-api
```
