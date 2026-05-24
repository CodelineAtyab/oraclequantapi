# Sequence Measurement API

A Spring Boot REST API that decodes a string encoding format and stores the results in an Oracle XE database.
 
---

## Encoding Format

Each input string is made up of back-to-back packages. The encoding rules are:

- Characters map to values: `_` = 0, `a` = 1, `b` = 2, ... `y` = 25, `z` = 26
- `z` is the only non-terminating character — it means "add 26 and keep reading"
- Any character that is not `z` terminates the current number
- Each package starts with a count, followed by exactly that many encoded values
- The result of each package is the sum of its decoded values
- The final output is a list of sums, one per package
### Example

| Input | Decoded | Output |
|-------|---------|--------|
| `abbcc` | Package 1: count=1, values=[2] → Package 2: count=2, values=[3,3] | `[2, 6]` |
| `czzabzc` | Package 1: count=3, values=[53, 2, 29] | `[53, 2, 29]` |
| `dz_a_aazzaaa` | Package 1: count=4, values=[26,1,0,1] → Package 2: count=1, values=[53] → Package 3: count=1, values=[1] | `[28, 53, 1]` |
 
---

## Architecture

The project is split into four layers:

- **`Sequence`** — pure domain object, no JPA annotations, holds business logic and validation
- **`SequenceHistory`** — JPA entity, owns all database mapping, converts to/from `Sequence` via `fromSequence()` and `toSequence()`
- **`SequenceService`** — business logic layer, bridges domain and persistence
- **`SequenceController`** — REST layer, exposes all endpoints
- **`SequenceRepo`** — JPA repository interface for Oracle XE
---

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/convert-measurements?input=abbcc` | Decode a new input string and store the result |
| `GET` | `/convert-measurements/{id}` | Fetch a specific record by ID |
| `GET` | `/convert-measurements/all` | Fetch all records as domain objects |
| `GET` | `/convert-measurements/history` | Fetch all raw records directly from the DB |
| `PUT` | `/convert-measurements/{id}` | Update the input of an existing record and re-decode |
| `DELETE` | `/convert-measurements/{id}` | Delete a specific record by ID |

### PUT Request Body
```json
{
    "input": "abbcc"
}
```
 
---

## Validation

- Only lowercase letters `a-z` and underscore `_` are allowed in the input
- Capital letters are automatically converted to lowercase
- Any other character (numbers, special characters) returns `"invalid sequence format"`
---

## Edge Cases

The decoder handles the following malformed inputs:

| Edge Case | Response |
|-----------|----------|
| Zero count package | `"0"` |
| Fewer values than count | `"malformed package: expected X values but found Y"` |
| Unterminated z-chain | `"malformed package: unterminated number"` |
| Z-chain exceeds 100 characters | `"malformed package: number exceeds maximum allowed size"` |
| Single character count with no values | `"malformed package: expected X values but found 0"` |
 
---

## Setup

### Prerequisites
- Java 17
- Spring Boot
- Oracle XE database
- Maven
### `application.properties`
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
 
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
spring.datasource.hikari.auto-commit=true
```

### Database Setup
Run the following in your Oracle XE SQL client:
```sql
CREATE TABLE SEQUENCES (
    ID          NUMBER(19)      NOT NULL,
    INPUT       VARCHAR2(4000),
    SOURCE_IP   VARCHAR2(100),
    TIMESTAMP   TIMESTAMP,
    OUTPUT      VARCHAR2(2000),
    CONSTRAINT PK_SEQUENCES PRIMARY KEY (ID)
);
 
CREATE SEQUENCE SEQUENCE_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
```

### Running the API Locally
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`
 
---

## Deploying as a Service on Oracle Linux

The API is packaged as `oraclequantapi-0.0.1.jar` and runs as a `systemd` service on Oracle Linux.

### Prerequisites
- Java 17 installed on the server
- The JAR file transferred to the server
### Step 1 — Verify Java is installed
```bash
java -version
```
If not installed, run:
```bash
sudo dnf install java-17-openjdk -y
```

### Step 2 — Create the application directory
```bash
sudo mkdir -p /opt/oraclequantapi
sudo cp oraclequantapi-0.0.1.jar /opt/oraclequantapi/oraclequantapi-0.0.1.jar
```

### Step 3 — Create the systemd service file
```bash
sudo nano /etc/systemd/system/oraclequantapi.service
```

Paste the following:
```ini
[Unit]
Description=Oracle Quant API Spring Boot Service
After=network.target
 
[Service]
User=root
WorkingDirectory=/opt/oraclequantapi
ExecStart=/usr/bin/java -jar /opt/oraclequantapi/oraclequantapi-0.0.1.jar
SuccessExitStatus=143
StandardOutput=journal
StandardError=journal
SyslogIdentifier=oraclequantapi
Restart=on-failure
RestartSec=10
 
[Install]
WantedBy=multi-user.target
```

### Step 4 — Reload systemd and enable the service
```bash
# Reload systemd to pick up the new service file
sudo systemctl daemon-reload
 
# Enable the service to start automatically on boot
sudo systemctl enable oraclequantapi
 
# Start the service
sudo systemctl start oraclequantapi
```

### Step 5 — Verify the service is running
```bash
sudo systemctl status oraclequantapi
```

You should see:
```
● oraclequantapi.service - Oracle Quant API Spring Boot Service
     Loaded: loaded (/etc/systemd/system/oraclequantapi.service; enabled)
     Active: active (running)
```

### Managing the Service

| Command | Description |
|---------|-------------|
| `sudo systemctl start oraclequantapi` | Start the service |
| `sudo systemctl stop oraclequantapi` | Stop the service |
| `sudo systemctl restart oraclequantapi` | Restart the service |
| `sudo systemctl status oraclequantapi` | Check service status |
| `sudo systemctl enable oraclequantapi` | Enable on boot |
| `sudo systemctl disable oraclequantapi` | Disable on boot |

### Viewing Logs
```bash
# View live logs
sudo journalctl -u oraclequantapi -f
 
# View last 100 lines
sudo journalctl -u oraclequantapi -n 100
```
 
---

## Example Requests

### Decode a new sequence
```
GET http://localhost:8080/convert-measurements?input=abbcc
```
Response:
```json
["2", "6"]
```

### Update an existing sequence
```
PUT http://localhost:8080/convert-measurements/1
```
Body:
```json
{
    "input": "czzabzc"
}
```
Response:
```json
["84"]
```

### Delete a sequence
```
DELETE http://localhost:8080/convert-measurements/1
```
Response:
```
Sequence 1 deleted successfully
```