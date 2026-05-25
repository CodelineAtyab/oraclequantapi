# OracleQuant ERP - Package Measurement Conversion API

Trainer-style Spring Boot API for converting encoded package measurement strings into package inflow totals. The application uses Java 17, Spring Boot, Maven, Oracle Linux, and Oracle XE.

## Features

- `GET /convert-measurements` converts encoded measurement input into a JSON array.
- Conversion history is stored in Oracle XE through Spring Data JPA.
- History can be fetched, updated, and cleared through REST endpoints.
- Console logs and rolling file logs are enabled.
- Log files keep one week of history.

## Project Structure

```text
src/main/java/om/oraclequant/pkc_api/OracleQuantPkcApiApplication.java
src/main/java/om/oraclequant/pkc_api/controllers/MeasurementController.java
src/main/java/om/oraclequant/pkc_api/controllers/HistoryController.java
src/main/java/om/oraclequant/pkc_api/services/MeasurementService.java
src/main/java/om/oraclequant/pkc_api/services/HistoryService.java
src/main/java/om/oraclequant/pkc_api/models/MeasurementSequence.java
src/main/java/om/oraclequant/pkc_api/models/MeasurementHistory.java
src/main/java/om/oraclequant/pkc_api/repositories/MeasurementHistoryRepository.java
src/main/resources/application.properties
version.txt
```

## Conversion Rules

- `a` to `z` represent `1` to `26`.
- `_` represents `0`.
- Uppercase letters are converted to lowercase.
- Characters other than letters and `_` are converted to `_`, so they count as zero.
- A sequence of `z` characters continues until the first non-`z` character.
- Each package starts with an encoded count, then up to that many encoded measurement values.
- If the count asks for more values than remain, the package uses only the remaining values.

Examples:

```text
aa                                      -> [1]
abbcc                                   -> [2, 6]
dz_a_aazzaaa                            -> [28, 53, 1]
a_                                      -> [0]
abcdabcdab                              -> [2, 7, 7]
abcdabcdab_                             -> [2, 7, 7, 0]
zdaaaaaaaabaaaaaaaabaaaaaaaabbaa        -> [34]
za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa        -> [40, 1]
a1                                      -> [0]
```

## Database Configuration

The application is configured for Oracle XE using Oracle Thin JDBC.

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
spring.jpa.show-sql=true
```

For Oracle XE, the common service name is:

```text
XEPDB1
```

If your Oracle installation uses SID instead, the URL may look like:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
```

## Running The Application

From the project root:

```bash
./mvnw spring-boot:run
```

Or build and run the jar:

```bash
./mvnw clean package
java -jar target/pkc-api.jar
```

The API runs on:

```text
http://localhost:8080
```

## REST API Endpoints

Convert measurements:

```bash
curl 'http://localhost:8080/convert?input=aa'
```

Response:

```json
[1]
```

The older query parameter name is also supported:

```bash
curl 'http://localhost:8080/convert?convert=aa'
```

Get all history records:

```bash
curl 'http://localhost:8080/history'
```

Get one history record:

```bash
curl 'http://localhost:8080/history/{id}'
```

Update one history record:

```bash
curl -X PUT 'http://localhost:8080/history/{id}' \
  -H 'Content-Type: application/json' \
  -d '{"input":"aa","output":"[1]"}'
```

Clear all history:

```bash
curl -X DELETE 'http://localhost:8080/history'
```

## Local No-Database Test

Before connecting Oracle, the conversion logic can be tested locally with:

```text
local-test/MeasurementLocalTest.java
```

This runner checks the conversion rules without starting Spring Boot and without using Oracle.

## Deploy On Oracle Linux Via SSH

Build the jar locally:

```bash
./mvnw clean package
```

Copy the jar to Oracle Linux:

```bash
scp target/pkc-api.jar opc@your-server-ip:/home/opc/pkc-api.jar
```

SSH into the Oracle Linux server:

```bash
ssh opc@your-server-ip
```

Install Java 17 if needed:

```bash
sudo dnf install -y java-17-openjdk
```

Set database environment values or edit `application.properties` before building:

```bash
export ORACLE_DB_URL='jdbc:oracle:thin:@localhost:1521/XEPDB1'
export ORACLE_DB_USERNAME='your_username'
export ORACLE_DB_PASSWORD='your_password'
```

Run the jar:

```bash
java -jar /home/opc/pkc-api.jar
```

Optional systemd service:

```ini
[Unit]
Description=OracleQuant Package Measurement API
After=network.target

[Service]
User=opc
WorkingDirectory=/home/opc
ExecStart=/usr/bin/java -jar /home/opc/pkc-api.jar
Restart=always
RestartSec=5

[Install]
WantedBy=multi-user.target
```

Save it as:

```text
/etc/systemd/system/pkc-api.service
```

Then run:

```bash
sudo systemctl daemon-reload
sudo systemctl enable --now pkc-api
sudo systemctl status pkc-api
```