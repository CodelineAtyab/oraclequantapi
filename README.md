# Measurement Conversion API

A Spring Boot REST API that parses and converts encoded measurement sequences into numeric results and stores request history in an Oracle XE database.

---

##  Overview

This service exposes endpoints to:

* Parse encoded measurement input strings
* Convert them into numeric package totals
* Store every request in an Oracle database
* Retrieve, update, and delete conversion history

---

## Setup

* Java 17
* Spring Boot 3.5.x
* Spring Web
* Spring Data JPA
* Oracle XE (JDBC)
* Maven

---

##  API Endpoints

### Convert Measurements

**GET** `/convert-measurements?input={string}`

Converts encoded measurement input into numeric results.

**Example:**

```
GET /convert-measurements?input=abczz_a
```

**Response:**

```json
[3, 28]
```

---

### History API

Base path: `/input`

#### Get all history

```
GET /input
```

#### Get history by ID

```
GET /input/{id}
```

#### Full update (PUT)

```
PUT /input/{id}
```

#### Partial update (PATCH)

```
PATCH /input/{id}
```

#### Delete all history

```
DELETE /input
```

---

## Encoding Rules

* `a = 1, b = 2, ..., z = 26`
* `_ = 0`
* First character of each package = counter (number of value slots)
* `'z'` introduces chaining: adds 26 + next character value recursively

Example:

```
dz_a_a → 28
```

---

## Database Setup

Uses Oracle XE with JPA.

Table: `MEASUREMENT_HISTORY`

Fields:

* ID (UUID)
* TIMESTAMP
* SOURCE_IP_ADDRESS
* INPUT
* OUTPUT

---

## Configuration

Update `application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@<host>:1521/XEPDB1
spring.datasource.username=system
spring.datasource.password=your_password
```

---

## Run Application

```bash
mvn spring-boot:run
```

Or:

```bash
mvn clean package
java -jar target/measurement-conversion-2.0.0.jar
```

---

## Project Structure

* `controllers` → REST endpoints
* `services` → business logic
* `models` → domain + entity classes
* `repositories` → JPA + in-memory store

---

## Logging

* Request logging via SLF4J
* Debug logs for parsing and conversion steps

---

## License

Internal / educational use (update as needed).
