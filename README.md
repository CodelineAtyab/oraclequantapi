# MARYAM Measurement Conversion API

A Spring Boot REST service that decodes encoded measurement strings into
numeric package totals and persists every request in Oracle XE.

---

## What it does

Send an encoded string to `/convert-measurements`.  The API decodes it into
a list of package totals and returns them as JSON.  Every request is also
written to an audit table in Oracle XE.

Example:

```
GET /convert-measurements?input=abcdabcdab    ->   [2, 7, 7]
GET /convert-measurements?input=dz_a_aazzaaa  ->   [28, 53, 1]
```

### Endpoints

| Method   | Path                                  | Purpose                                |
|----------|---------------------------------------|----------------------------------------|
| GET      | `/convert-measurements?input=...`     | Decode + persist                       |
| GET      | `/history`                            | List all history records               |
| GET      | `/history/{id}`                       | One history record                     |
| PUT      | `/history/{id}`                       | Replace a history record               |
| PATCH    | `/history/{id}`                       | Partial update                         |
| DELETE   | `/history/{id}`                       | Delete one history record              |
| DELETE   | `/history`                            | Clear the entire history table         |
| GET      | `/swagger-ui/index.html`              | Interactive API docs                   |
| GET      | `/actuator/health`                    | Liveness probe                         |

Base URL when running locally:  `http://localhost:8080/maryam`

---

## Tech stack

Java 17  ·  Spring Boot 3.2  ·  Spring Data JPA  ·  Oracle XE 21c  ·
SpringDoc OpenAPI  ·  Logback  ·  Maven.

---

## How to set it up

### 1. Prerequisites

- Oracle OpenJDK 17
- Maven 3.9+
- Oracle XE 21c reachable at `localhost:1521/XEPDB1`
- SYS/SYSTEM password set to `1234`  (matches `application.properties`)

### 2. Build

```bash
mvn clean package
```

Produces `target/maryam-measurement-api.jar`.

### 3. Run

```bash
java -jar target/maryam-measurement-api.jar
```

Service starts on `http://localhost:8080/maryam`.

### 4. Try it

```bash
curl "http://localhost:8080/maryam/convert-measurements?input=aa"        # [1]
curl  http://localhost:8080/maryam/history
```

Open Swagger:  `http://localhost:8080/maryam/swagger-ui/index.html`

---

## Deploying on Oracle Linux

See `LINUX_DEPLOY.md` for the full step-by-step.  Short version:

```bash
# on the Oracle Linux box
sudo dnf install -y java-17-openjdk maven
unzip maryam-measurement-api.zip && cd maryam-measurement-api
mvn clean package -DskipTests
sudo cp target/maryam-measurement-api.jar           /opt/maryam-measurement-api/
sudo cp src/main/resources/application.properties /opt/maryam-measurement-api/
sudo cp deployment/maryam-measurement-api.service   /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable --now maryam-measurement-api
```

The PDF requires the jar to be running as a managed service on Oracle Linux
reachable over SSH - the unit file above does exactly that.

---

## Configuration

`src/main/resources/application.properties`:

| Property                          | Default                                              |
|-----------------------------------|------------------------------------------------------|
| `server.port`                     | 8080                                                 |
| `server.servlet.context-path`     | `/maryam`                                              |
| `spring.datasource.url`           | `jdbc:oracle:thin:@//localhost:1521/XEPDB1`          |
| `spring.datasource.username`      | `SYSTEM`                                             |
| `spring.datasource.password`      | `1234`                                               |
| `spring.jpa.hibernate.ddl-auto`   | `update` (auto-creates the history table)            |
| Log file                          | `logs/maryam-measurement-api.log` (rolling, 30 days)   |

For a quick demo without Oracle, run with the H2 profile:

```bash
java -jar target/maryam-measurement-api.jar --spring.profiles.active=dev
```

---

## Project layout

```
src/main/java/om/maryam/measurement/
├── MaryamMeasurementApplication.java
├── algorithm/      decoder (pure, unit-tested)
├── controller/     REST endpoints
├── service/        business contracts + impl
├── repository/     Spring Data JPA
├── entity/         JPA entity
├── dto/            request/response DTOs
├── exception/      domain exceptions + global handler
├── config/         OpenAPI metadata
└── util/           client-IP resolver
```

Architecture is layered (controller -> service -> repository -> Oracle),
each layer depends only on the one beneath it through interfaces.

---

## Running tests

```bash
mvn test
```

`MeasurementDecoderTest` runs all eight examples from the evaluation document
and blocks the build if any of them regress.
