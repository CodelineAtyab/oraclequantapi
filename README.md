# OracleQuant ERP - Package Measurement Conversion API

A REST API for converting measurement input strings into package totals, built with **Java 17**, **Spring Boot**, **JPA**, and **Oracle XE**.

---

## Prerequisites

- Oracle OpenJDK 17
- Maven (or use the included `mvnw` wrapper)
- Oracle XE database (21c+)

---

## Build

```bash
./mvnw clean package
```

Produces `target/oraclequantapi-0.0.1-SNAPSHOT.jar`.

---

## Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
```

Tables are created automatically via `ddl-auto=update`.

---

## Run

```bash
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

The API starts at `http://localhost:8080`.

---

## API Endpoints

### Convert measurements

```
GET /convert-measurements?input={string}
```

**Response**: `200 OK` with JSON array of package totals.

| Request | Response |
|---|---|
| `?input=aa` | `[1]` |
| `?input=abbcc` | `[2, 6]` |
| `?input=dz_a_aazzaaa` | `[28, 53, 1]` |
| `?input=a_` | `[0]` |
| `?input=abcdabcdab` | `[2, 7, 7]` |
| `?input=abcdabcdab_` | `[2, 7, 7, 0]` |

### History

| Method | Endpoint | Description |
|---|---|---|
| GET | `/convert-measurements/history` | List all records |
| GET | `/convert-measurements/history/{id}` | Get record by ID |
| PUT | `/convert-measurements/history/{id}` | Update record |
| DELETE | `/convert-measurements/history` | Clear all history |
| DELETE | `/convert-measurements/history/{id}` | Delete record by ID |

Each history record contains: `id`, `timestamp`, `source_ip_address`, `input`, `output`.

---

## Deploy on Oracle Linux

### 1. Transfer the JAR

```bash
scp target/oraclequantapi-0.0.1-SNAPSHOT.jar oracle@your-vm-ip:/home/oracle/
```

### 2. SSH into the VM

```bash
ssh oracle@your-vm-ip
```

### 3. Install Java (if not present)

```bash
sudo dnf install java-17-openjdk-devel
```

### 4. Run as a service (optional)

```bash
sudo tee /etc/systemd/system/oraclequantapi.service <<EOF
[Unit]
Description=OracleQuant Measurement Conversion API
After=network.target

[Service]
ExecStart=/usr/bin/java -jar /home/oracle/oraclequantapi-0.0.1-SNAPSHOT.jar
User=oracle
Restart=always

[Install]
WantedBy=multi-user.target
EOF

sudo systemctl daemon-reload
sudo systemctl enable --now oraclequantapi
```

### 5. Verify

```bash
curl http://localhost:8080/convert-measurements?input=aa
```

---

## Encoding Rules

- `a` = 1, `b` = 2, ..., `z` = 26
- `_` = 0
- Numbers > 26: chain `z` characters (each adds 26), terminated by a non-`z` letter
- Each package: first number = count of values, then sum of that many values

---

## Logging

Logs are written to `logs/oraclequantapi.YYYY-MM-DD.log` and rotated daily, retained for 7 days.
