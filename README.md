## Project Overview

Oracle Quant API is a Spring Boot 3.5.14 / Java 17 REST API that decodes submitted sequence strings using a self-delimiting length-encoded parser and persists the results in an Oracle database.

**Tech Stack**
- Java 17
- Spring Boot 3.5.14
- Spring Web (embedded Tomcat)
- Spring Data JPA / Hibernate ORM (OracleDialect)
- Oracle JDBC (ojdbc11)
- Maven

---

## How to Run

**Prerequisites:** Java 17 JDK and an Oracle database configured in `application.yaml` (see Database Setup below).

### Run from source

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

### Run the JAR (v1.0)

A pre-built executable JAR is available.

```bash
# Windows
java -jar oraclequantapi-1.0.jar

# macOS / Linux
java -jar oraclequantapi-1.0.jar
```

To override the datasource without modifying the bundled config, place an `application.yaml` in the same directory as the JAR — Spring Boot will pick it up automatically:

```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@//your-host:1521/your-service
    username: YOUR_USERNAME
    password: YOUR_PASSWORD
```

The server starts on `http://localhost:8080`.

---

## Architecture

Clean 3-layer architecture with strict Single Responsibility Principle (SRP):

```
HTTP Request
     |
     v
[ Controller ]        -- HTTP mappings only, zero business logic
     |
     v
[   Service  ]        -- Input validation and decoder algorithm
     |
     v
[Sequence_DATABASE]   -- Exception-safe Oracle persistence wrapper
     |
     v
[  Repository  ]      -- Spring Data JPA interface
     |
     v
[  Oracle DB  ]       -- SEQUENCE_ENQUIRIES table
```

**Package structure:**
```
com.oraclequantapi.oraclequantapi
+-- OraclequantapiApplication.java   (entry point)
+-- controller/
|   +-- Controller.java              (REST layer - @RestController)
+-- services/
|   +-- Service.java                 (business logic - @Service)
+-- module/
|   +-- Sequence.java                (JPA entity mapped to SEQUENCE_ENQUIRIES)
+-- repository/
    +-- Repository.java              (Spring Data JPA interface)
    +-- Sequence_DATABASE.java       (exception-safe Oracle persistence wrapper)
```

- The Controller delegates all logic to the Service via `@Autowired` injection.
- The Service validates input, runs the length-encoded parser, and delegates all persistence to `Sequence_DATABASE`.
- `Sequence.java` is the JPA `@Entity` mapped to the `SEQUENCE_ENQUIRIES` Oracle table. It owns all column mappings and JSON serialization rules.
- `id` and `currentTime` are always server-generated — never client-supplied.
- `input` is write-only — accepted in the request body but never returned in any response.
- Data persists across restarts via Oracle; the table is auto-created on first startup (`ddl-auto: update`).

---

## Database Diagram

```
+-----------------------------+
|   SEQUENCE_ENQUIRIES        |  Oracle Table
+-----------------------------+
| PK  ID           VARCHAR2   |  UUID, server-generated
|     INPUT        VARCHAR2   |  Raw input (write-only in API)
|     CURRENT_TIME VARCHAR2   |  Timestamp of save or last update
+-----------------------------+
           |
           | managed by
           v
+-----------------------------+
|   Repository.java           |  Spring Data JPA interface
|   JpaRepository<            |  Auto-provides: save, findAll,
|     Sequence, String>       |  existsById, deleteById
+-----------------------------+
           |
           | wrapped by
           v
+-----------------------------+
|   Sequence_DATABASE.java    |  Exception-safe persistence layer
|   persist()                 |  -> repository.save()
|   retrieveAll()             |  -> repository.findAll()
|   update()                  |  -> existsById() + save()
|   remove()                  |  -> existsById() + deleteById()
+-----------------------------+
           |
           | called by
           v
+-----------------------------+
|   Service.java              |  Business logic layer
|   addSequence()             |  -> persist()
|   getAllSequences()         |  -> retrieveAll()
|   updateSequence()          |  -> update()
|   deleteSequence()          |  -> remove()
+-----------------------------+
```

---

## Database Setup

1. **Configure credentials** in `src/main/resources/application.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:oracle:thin:@//your-host:1521/your-service
       username: YOUR_USERNAME
       password: YOUR_PASSWORD
   ```

2. **Start the application** — `ddl-auto: update` will auto-create the `SEQUENCE_ENQUIRIES` table on first startup. No manual SQL required.

---

## Decoder Algorithm

The `sequenceLogicAlgorithm` method processes the input string using a self-delimiting length-encoded parser:

- Character values: `a=1, b=2, ... z=26, _=0`
- **Header phase:** consecutive `z` characters each add 26 to the block length; the first non-`z` character adds its own value. This determines how many characters to consume next.
- **Data phase:** consume exactly that many characters and sum their values; the sum is appended to the output array.
- Parsing repeats left-to-right until the full string is consumed.

**Examples:**

| Input | Decode steps | Output |
|---|---|---|
| `abbcc` | `a`=len 1 → `b`=2; `b`=len 2 → `c`+`c`=6 | `[2, 6]` |
| `cdaaabaa` | `c`=len 3 → `d`+`a`+`a`=6; `a`=len 1 → `b`=2; `a`=len 1 → `a`=1 | `[6, 2, 1]` |
| `zabc...` | `z`+`a`=len 27 → consume 27 chars | `[sum]` |

---

## API Reference

### POST `/sequenceDecoder`

Submit a sequence string for decoding. The `input` field must contain **only lowercase letters a-z and underscores** and **must not start with `_`** — any violation returns 400. The server decodes the input, auto-generates `id` and `currentTime`, and returns the result.

**Request:**
```http
POST http://localhost:8080/sequenceDecoder
Content-Type: application/json

{
  "input": "abbcc"
}
```

**Response — 201 Created:**
```json
{
  "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001",
  "currentTime": "2026-05-24 14:30:00",
  "output": [2, 6]
}
```

**Another valid example:**
```http
{ "input": "cdaaabaa" }
```
```json
{
  "id": "b7e2d3a1-91cd-4f22-c4d8-3g5500000002",
  "currentTime": "2026-05-24 14:31:05",
  "output": [6, 2, 1]
}
```

**Invalid input — Response 400 Bad Request:**
```json
{ "input": "Hello123!" }
```
```json
{ "input": "_abc" }
```
```
Input must only contain a-z and underscore, and must not start with underscore
```

---

### PUT `/sequenceDecoder`

Update an existing enquiry by `id`. Applies the same input validation rules as POST. Re-runs the decoder on the new input and refreshes `currentTime`.

**Request:**
```http
PUT http://localhost:8080/sequenceDecoder
Content-Type: application/json

{
  "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001",
  "input": "cdaaabaa"
}
```

**Response — 201:**
```json
{
  "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001",
  "currentTime": "2026-05-24 15:00:00",
  "output": [6, 2, 1]
}
```

**Failure — 400 Bad Request:**
- `id` not found, or `input` fails validation:
```
Enquiry not found or input invalid
```

---

### DELETE `/sequenceDecoder`

Remove an existing enquiry by `id`.

**Request:**
```http
DELETE http://localhost:8080/sequenceDecoder
Content-Type: application/json

{
  "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001"
}
```

**Response — 201:**
```
Enquiry deleted successfully
```

**Failure — 400 Bad Request:**
```
Enquiry not found or already deleted
```

---

### GET `/sequenceDecoder`

Retrieve all stored sequence enquiries with their decoded outputs.

**Request:**
```http
GET http://localhost:8080/sequenceDecoder
```

**Response — 200 OK:**
```json
[
  {
    "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001",
    "currentTime": "2026-05-24 14:30:00",
    "output": [2, 6]
  },
  {
    "id": "b7e2d3a1-91cd-4f22-c4d8-3g5500000002",
    "currentTime": "2026-05-24 14:31:05",
    "output": [6, 2, 1]
  }
]
```
