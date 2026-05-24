## Submission Instructions

To submit your Oracle JAVA Spring Boot Maven project as a solution, please follow these steps:

### Step 1: Install git on your PC
- Install "git" as shown in this tutorial: [How to install git](https://youtu.be/iYkLrXobBbA?si=_l0haibv_X9NpIjJ)
- Open command prompt and run
  ```bash
  git version
  ```
- If you see the version, then git is successfully installed.

### Step 2: Fork the Repository
- Navigate to [this repository](https://github.com/CodelineAtyab/oraclequantapi) provided by Codeline.
- Click on the "Fork" button at the top-right corner of the page to create a copy of the repository under your own GitHub account.

### Step 3: Clone the Forked Repository
- Open your terminal or command prompt.
- Clone the forked repository to your local machine using the following command:
  ```bash
  git clone https://github.com/your-username/repo-name.git
  ```

### Step 4: Create a new branch
- Navigate to the cloned repository directory
  ```bash
  cd repo-name
  ```
- Create a new branch for your code submissions (Replace your-name with your name in your-name-submission-branch):
  ```bash
  git checkout -b your-name-submission-branch
  ```


### Step 5: Add Your Code
- Implement the API

### Step 6: Commit your changes
- Run the following commands in order to commit your changes:
  ```bash
  git add *
  git commit -m "Meaningful commit message here"
  ```

### Step 7: Push Your Branch to GitHub
- Run the following commands to upload the changes to the forked github repository (Replace your-name with your name in your-name-submission-branch):
  ```bash
  git push origin your-name-submission-branch
  ```

### Step 8: Create a Pull Request
- Go to your forked repository on GitHub.
- You should see a prompt to create a pull request. Click on "Compare & pull request".
- Provide a title and description for your pull request, then click "Create pull request".

### Step 9: Notify Codeline
- Notify on slack that you have created a PR for your solution.

## Note: If you face any issues in the process above, Please do the following:
- Watch [this youtube tutorial](https://www.youtube.com/watch?v=a_FLqX3vGR4)
- Contact Ikhlas or Atyab.
----------------# OracleQuant API - Package Measurement Conversion

## How to Build
```bash
cd oraclequantapi
mvn clean package -DskipTests
```

## How to Run
```bash
java -jar target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

## Database Configuration
Make sure Oracle XE is running via Docker:
```bash
docker run -d --name oracle-xe -p 1521:1521 -e ORACLE_PASSWORD=29999login gvenzl/oracle-xe:21-slim
```

Update `application.properties`:

# OracleQuant ERP - Package Measurement Conversion API

## Overview
A REST API for converting package measurement input strings into a list of total values per package. Built with Java 17, Spring Boot, and Oracle XE Database.

---

## Prerequisites
- Oracle OpenJDK 17
- Oracle XE Database
- Maven 3.x

---

## Building the Application

```bash
./mvnw clean package
```

The JAR file will be generated at:
```
target/oraclequantapi-0.0.1-SNAPSHOT.jar
```

---

## Configuring the Database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=system
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.jpa.hibernate.ddl-auto=update
```

---

## Running the Application

### On Windows:
```bash
./mvnw spring-boot:run
```

### On Oracle Linux:
```bash
java -jar oraclequantapi-0.0.1-SNAPSHOT.jar
```

The application runs on port **8080** by default.

---

## Deploying on Oracle Linux via SSH

### Step 1 — Copy JAR to Oracle Linux server:
```bash
scp -P 22 target/oraclequantapi-0.0.1-SNAPSHOT.jar user@SERVER_IP:/home/user/
```

### Step 2 — SSH into the server:
```bash
ssh user@SERVER_IP
```

### Step 3 — Run the application:
```bash
java -jar /home/user/oraclequantapi-0.0.1-SNAPSHOT.jar
```

### Step 4 — Run in background (keep running after logout):
```bash
nohup java -jar /home/user/oraclequantapi-0.0.1-SNAPSHOT.jar > /dev/null 2>&1 &
```

---

## REST API Endpoints

### Conversion Endpoint

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/convert-measurements?input={string}` | Convert measurement input string |

**Example:**
```
GET http://localhost:8080/convert-measurements?input=abbcc
Response: [2, 6]
```

---

### History Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/history` | Get all history records |
| GET | `/history/{id}` | Get record by ID |
| PUT | `/history/{id}` | Update record by ID |
| PATCH | `/history/{id}` | Partially update record by ID |
| DELETE | `/history` | Delete all history records |

**Example:**
```
GET http://localhost:8080/history
Response:
[
  {
    "id": 1,
    "timestamp": "2026-05-24T00:48:17",
    "sourceIpAddress": "0:0:0:0:0:0:0:1",
    "input": "abbcc",
    "output": "[2, 6]"
  }
]
```

---

## Input Encoding Format

- Letters `a–y` map to values `1–25`
- Letter `z` means continue reading (add next character too)
- `_` means `0`
- First character = count of values in the package
- Remaining characters = the values

**Examples:**

| Input | Output |
|-------|--------|
| `aa` | `[1]` |
| `abbcc` | `[2, 6]` |
| `dz_a_aazzaaa` | `[28, 53, 1]` |
| `a_` | `[0]` |

---

## Logging

Logs are written to:
```
logs/pkc-api.log
```
- Rolling logs: 7 days history
- Log level: INFO

---

## Project Structure

```
src/
├── main/
│   ├── java/com/oraclequantapi/oraclequantapi/
│   │   ├── controller/
│   │   │   ├── ConversionController.java
│   │   │   └── HistoryController.java
│   │   ├── model/
│   │   │   └── ConversionHistory.java
│   │   ├── repository/
│   │   │   └── HistoryRepository.java
│   │   ├── service/
│   │   │   ├── ConversionService.java
│   │   │   └── HistoryService.java
│   │   └── OraclequantapiApplication.java
│   └── resources/
│       └── application.properties
```
