# Oracle Quant API

## Project Overview

Oracle Quant API is a Spring Boot backend application that processes encoded measurement strings and converts them into readable numerical package totals.

The project was designed using clean layered architecture and follows object oriented programming principles such as abstraction, encapsulation, and separation of responsibilities.

The application supports:

* Encoded measurement parsing
* REST API communication
* Validation and centralized exception handling
* Logging with rolling log files
* Oracle database persistence
* Measurement history retrieval

The application uses Oracle Database running inside Docker and persists all successful conversion requests.

---

## Technologies Used

* Java 17
* Spring Boot
* Maven
* Spring Data JPA
* Hibernate
* Oracle Database XE
* Docker
* SLF4J Logging
* Logback

---

## Features

* Encoded measurement parsing
* REST API architecture
* Oracle database persistence
* Measurement history retrieval
* Centralized exception handling
* Rolling application logs

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

## Oracle Linux Deployment

This project was packaged into a deployable Spring Boot JAR file and tested successfully outside IntelliJ.

### Build the Application

Run the following command inside the project directory:

```bash
mvn clean package -DskipTests
```

After a successful build, the JAR file will be generated inside:

```text
target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

### Run the Application

Start the Spring Boot application using:

```bash
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

### Oracle Database Setup

Oracle Database was configured using Docker.

Start the Oracle XE container:

```bash
docker run -d \
--name oracle-db \
-p 1521:1521 \
-e ORACLE_PASSWORD=oracle \
gvenzl/oracle-xe:21-slim
```

### Verify Database Connection

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

### API Testing

The API was tested using Postman.

Example request:

```http
GET http://localhost:8080/convert-measurements?input=dz_a_aazzaaa
```

Example response:

```json
[28,53,1]
```