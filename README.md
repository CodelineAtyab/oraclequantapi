# Oracle Quant API

## Project Overview

Oracle Quant API is a Spring Boot backend application that processes encoded measurement strings and converts them into readable numerical package totals.

The project was designed using clean layered architecture and follows object-oriented programming principles such as abstraction, encapsulation, inheritance, polymorphism, and separation of responsibilities.

The application supports:

* Encoded measurement parsing
* REST API communication
* Validation and centralized exception handling
* Logging with rolling log files
* Oracle database persistence
* Measurement history retrieval and management

The application uses Oracle Database XE and persists all successful conversion requests.

---

## Technologies Used

* Java 17
* Spring Boot
* Maven
* Spring Data JPA
* Hibernate
* Oracle Database XE
* Oracle Linux
* Docker
* SLF4J Logging
* Logback

---

## Features

* Encoded measurement parsing
* REST API architecture
* Oracle database persistence
* Measurement history retrieval
* Measurement history deletion
* Centralized exception handling
* Rolling application logs
* Oracle Linux deployment support

---

## Design Principles

The application follows:

* Single Responsibility Principle (SRP)
* Encapsulation
* Abstraction
* Interface-driven development
* Layered architecture
* Separation of concerns

---

## Project Structure

```text
controller/
service/
service/impl/
repository/
entity/
parser/
exception/
```

---

## Oracle Database Configuration

Database configuration is located inside:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XE
spring.datasource.username=system
spring.datasource.password=oracle
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Oracle Database Setup

Oracle Database XE was configured using Docker.

Start the Oracle XE container:

```bash
docker run -d \
--name oracle-db \
-p 1521:1521 \
-e ORACLE_PASSWORD=oracle \
gvenzl/oracle-xe:21-slim
```

Verify the running container:

```bash
docker ps
```

Access the Oracle container:

```bash
docker exec -it oracle-db bash
```

Connect to SQL Plus:

```bash
sqlplus system/oracle@localhost:1521/XE
```

Verify stored records:

```sql
SELECT * FROM measurement_record;
```

---

## Build the Application

Run the following command inside the project directory:

```bash
mvn clean package -DskipTests
```

After a successful build, the JAR file will be generated inside:

```text
target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

---

## Run the Application

Start the Spring Boot application using:

```bash
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

The application runs on:

```text
http://localhost:8080
```

---

## REST API Endpoints

### Convert Measurements

```http
GET /convert-measurements?input=dz_a_aazzzaaa
```

Example:

```bash
curl "http://localhost:8080/convert-measurements?input=dz_a_aazzzaaa"
```

Example Response:

```json
[28,53,1]
```

---

### Get All Measurement History

```http
GET /measurement-history
```

Example:

```bash
curl "http://localhost:8080/measurement-history"
```

---

### Get Measurement History By ID

```http
GET /measurement-history/{id}
```

Example:

```bash
curl "http://localhost:8080/measurement-history/1"
```

---

### Update Measurement History

```http
PUT /measurement-history/{id}
```

Example:

```bash
curl -X PUT "http://localhost:8080/measurement-history/1" \
-H "Content-Type: application/json" \
-d '{"input":"updated_input","output":"[10,20]"}'
```

---

### Delete Measurement History

```http
DELETE /measurement-history/{id}
```

Example:

```bash
curl -X DELETE "http://localhost:8080/measurement-history/1"
```

Example verification:

```bash
curl "http://localhost:8080/measurement-history"
```

---

## Logging

Application logs are written both to the console and rolling log files.

Log files are stored inside:

```text
logs/application.log
```

Rolling log history is configured for one week retention using Logback rolling policies.

Example configuration:

```properties
logging.file.name=logs/application.log
logging.logback.rollingpolicy.max-file-size=10MB
logging.logback.rollingpolicy.max-history=7
```

---

## Oracle Linux Deployment

This project was packaged into a deployable Spring Boot JAR file and tested successfully on Oracle Linux.

### Connect to Oracle Linux via SSH

```bash
ssh username@server-ip
```

---

### Clone the Repository

```bash
git clone <repository-url>
```

Navigate into the project:

```bash
cd oraclequantapi
```

---

### Build the Project on Oracle Linux

```bash
mvn clean package -DskipTests
```

---

### Run the Application on Oracle Linux

```bash
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

---

### Verify the API

```bash
curl "http://localhost:8080/convert-measurements?input=dz_a_aazzzaaa"
```

Expected response:

```json
[28,53,1]
```

---

## Exception Handling

The application uses centralized exception handling for invalid measurement inputs and unexpected server errors.

Custom exceptions are located inside:

```text
exception/
```

---

## Version

Current Version:

```text
v1.0.0
```

### Features Included

* Measurement conversion endpoint
* Oracle database persistence
* Measurement history endpoints
* Logging support
* Oracle Linux deployment
* REST API architecture
* Centralized exception handling

---

## Changelog

### v1.0.0

* Initial project release
* Implemented encoded measurement conversion
* Added Oracle XE database persistence
* Added measurement history endpoints
* Added rolling logging support
* Added Oracle Linux deployment support
* Added centralized exception handling

---

## Development Environment

Developed using Java 17, Spring Boot, Oracle XE Database, and Oracle Linux deployment environment.