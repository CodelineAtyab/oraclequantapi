# Package Measurement Conversion API

## Features
- Convert measurement strings to numeric package totals.
- Persist conversion history in Oracle XE Database.
- Built with Spring Boot and Oracle OpenJDK 17.
- Logging to `logs/` directory.

## Prerequisites
- Oracle OpenJDK 17 installed.
- Oracle XE 21c Database running.
- Apache Maven 3.8+ installed.

---

## Running the Application

### Build the JAR
```bash
.\mvnw clean package -DskipTests
```

### Run the JAR
```bash
java -jar target/pkc-api.jar
```

### API available at

http://localhost:8080/
---

## Configuring the Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
spring.datasource.username=system
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

---

## API Endpoints

### Convert Measurements

GET/http://192.168.100.7:8080/history/2

{
    "id": 2,
    "timestamp": "2026-05-24T08:59:18.375786",
    "sourceIpAddress": "0:0:0:0:0:0:0:1",
    "input": "aa",
    "output": "[1]"
}

POST/http://192.168.100.7:8080/history/2

"timestamp": "2026-05-24T12:18:33.642+00:00",
"status": 405,
"error": "Method Not Allowed",
"path": "/history/2"
}

PATCH/http://192.168.100.7:8080/history/

{
"timestamp": "2026-05-24T12:19:48.149+00:00",
"status": 404,
"error": "Not Found",
"path": "/history/"
}

PUT/http://localhost:8080/history/1
{
"id": 1,
"timestamp": "2026-05-24T08:58:56.620227",
"sourceIpAddress": "0:0:0:0:0:0:0:1",
"input": "abbcc",
"output": "[2, 6]"
}

http://192.168.100.7:8080/api/convert-measurements?input=abbcc
[2,6]

DELETE/history

--- Example Response:
```json
[
  {
    "id": 1,
    "timestamp": "2024-05-24T10:30:00",
    "sourceIpAddress": "127.0.0.1",
    "input": "abbcc",
    "output": "[2, 6]"
  }
]
```

---

## Deploy on Oracle Linux via SSH

### 1. Copy JAR to Oracle Linux
```bash
scp target/pkc-api.jar razan@192.168.100.7:/home/razan/
```

### 2. SSH into server
```bash
ssh razan@192.168.100.7
```

### 3. Install Java 17
```bash
sudo dnf install -y java-17-openjdk-headless
```

### 4. Create app folder and move JAR
```bash
sudo mkdir -p /opt/pkc-api
sudo cp /home/razan/pkc-api.jar /opt/pkc-api/
```

### 5. Create configuration file
```bash
sudo tee /opt/pkc-api/application.properties << 'EOF'
spring.datasource.url=jdbc:oracle:thin:@//192.168.100.11:1521/XEPDB1
spring.datasource.username=system
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.hibernate.ddl-auto=update
server.port=8080
EOF
```

### 6. Open firewall port
```bash
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

### 7. Run the application
```bash
java -jar /opt/pkc-api/pkc-api.jar \
  --spring.config.location=file:/opt/pkc-api/application.properties
```

### 8. Test the deployment
```bash
curl "http://http://192.168.100.7:8080/api/convert-measurements?input=abcdabcdab"
```

--- Response:[2,7,7]
