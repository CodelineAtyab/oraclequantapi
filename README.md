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