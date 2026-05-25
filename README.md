# History Conversion API

A Spring Boot REST API that converts input strings into numeric values and stores the conversion history in an Oracle XE database.

---

## Features

Convert input strings into numeric output
Save conversion history in Oracle XE
Get all history records
Get a history record by ID
Update a history record
Delete a history record
REST API using Spring Boot

---

## Technologies Used

Java 17
Spring Boot
Maven
Oracle XE
Docker
Postman

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/convert-measurements?input=aa` | Convert input and save history |
| GET | `/history` | Get all history |
| GET | `/history/{id}` | Get history by ID |
| PUT | `/history/{id}` | Update history |
| DELETE | `/history/{id}` | Delete history |

---

## Example Request

### Convert Input
```http
GET http://localhost:8080/convert-measurements?input=aa
```

### Example Response
```json
[1]
```

---

## Update Example

### Request
```http
PUT http://localhost:8080/history/1
```

### Body
```json
{
  "input": "updated",
  "output": "[99]",
  "sourceIpAddress": "127.0.0.1",
  "timestamp": "updated"
}
```

---

## Delete Example

### Request
```http
DELETE http://localhost:8080/history/1
```

### Response
```text
History record deleted successfully
```

---

## Database Configuration

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=system
spring.datasource.password=29999login
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Running the Project

```bash
mvn spring-boot:run
```

The application will run on:

```text
http://localhost:8080
```

---

## Testing

The API was tested using Postman for:
GET
PUT
DELETE

All endpoints worked successfully.