# Oracle Quant ERP API — Measurement Converter

A robust, enterprise-grade Backend RESTful API built using Spring Boot 3 and Java 17. This service is designed as an evaluation module for ERP system integration, specialized in parsing, decoding, and processing complex measurement sequence strings into standard numeric arrays. It features fully automated transaction logging, persisting all operations into an Oracle Database via Hibernate ORM.

---

## Tech Stack & Ecosystem

- **Language:** Java 17 (JDK 17)
- **Framework:** Spring Boot 3.5.14
- **Build Tool:** Maven
- **Database:** Oracle Database Express Edition (XE) via Docker Container
- **Data Access Layer:** Spring Data JPA / Hibernate ORM
- **Testing & Administration:** Postman & DbVisualizer

---

## Prerequisites

Ensure you have the following environment components installed and running before starting the application:
1. Java 17 JDK configured in your system path.
2. Docker Desktop running locally to host the containerized database.
3. Postman for endpoint verification and API testing.
4. DbVisualizer (or equivalent database tool) to monitor transactional tables.

---

## Local Setup & Execution Guide

### 1. Initialize Oracle Database via Docker
Run the following command in your terminal to pull and boot up the Oracle XE database container configured for this project:

- docker run -d --name oracle-db -p 1521:1521 -e ORACLE_PASSWORD=29999login gvenzl/oracle-xe

### 2. Configure Environment Properties
The live application properties profile located at src/main/resources/application.properties is configured as follows to map the container environment:

- spring.application.name=oraclequantapi
- spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
- spring.datasource.username=system
- spring.datasource.password=29999login
- spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
- spring.jpa.database-platform=org.hibernate.dialect.OracleDialect
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.show-sql=true

### 3. Build & Compile the Executable Artifact (JAR)
To clean previous builds and package the application into a standalone runnable artifact while ensuring database context tests pass cleanly, execute:

- ./mvnw clean package

**Output Destination:** The executable file will be successfully generated inside the /target directory as oraclequantapi-0.0.1-SNAPSHOT.jar.

### 4. Run the Application Server
To boot up the embedded web application server directly via the terminal interface, run:

- ./mvnw spring-boot:run

Upon successful initialization, the console log will confirm the listener port:
Tomcat started on port 8080 (http) with context path '/'