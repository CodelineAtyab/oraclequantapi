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

---

## Project Overview

Oracle Quant API is a Spring Boot 3.5.14 / Java 17 REST API that accepts and stores sequence enquiries in memory.

**Tech Stack**
- Java 17
- Spring Boot 3.5.14
- Spring Web (embedded Tomcat)
- Maven

---

## How to Run

**Prerequisites:** Java 17 JDK installed.

```bash
# Windows
mvnw.cmd spring-boot:run

# macOS / Linux
./mvnw spring-boot:run
```

The server starts on `http://localhost:8080`. No database or external setup required.

---

## Architecture

Clean 3-layer architecture with strict Single Responsibility Principle (SRP):

```
HTTP Request
     |
     v
[ Controller ]   -- HTTP mappings only, zero business logic
     |
     v
[   Service  ]   -- All business logic, in-memory ArrayList storage
     |
     v
[  Sequence  ]   -- POJO model (id, sequence, currentTime)
```

**Package structure:**
```
com.oraclequantapi.oraclequantapi
├── OraclequantapiApplication.java   (entry point)
├── controller/
│   └── Controller.java              (REST layer - @RestController)
└── service/
    ├── Service.java                 (business logic - @Service)
    └── Sequence.java                (model POJO)
```

- The Controller delegates all logic to the Service via `@Autowired` injection.
- The Service stores all incoming sequences in an in-memory `ArrayList` (data resets on restart).
- `id` and `currentTime` are always server-generated — never client-supplied.

---

## API Reference

### POST `/sequenceDecoder`

Submit a new sequence enquiry. The `input` field must contain **only lowercase letters a–z and underscores** — any other character returns 400. The server auto-generates `id` (UUID) and `currentTime`.

**Request:**
```http
POST http://localhost:8080/sequenceDecoder
Content-Type: application/json

{
  "input": "aabbbbbbbabdchdb"
}
```

**Response — 201 Created:**
```json
{
  "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001",
  "input": "aabbbbbbbabdchdb",
  "currentTime": "2026-05-24 14:30:00"
}
```

**Another valid example:**
```json
{ "input": "aa_" }
```

**Invalid input — Response 400 Bad Request:**
```json
{ "input": "Hello123!" }
```
```
Input must only contain lowercase letters a-z and underscore
```

---

### GET `/sequenceDecoder`

Retrieve all stored sequence enquiries.

**Request:**
```http
GET http://localhost:8080/sequenceDecoder
```

**Response — 200 OK:**
```json
[
  {
    "id": "a3f9c1d2-84ab-4e11-b3c7-2f4400000001",
    "input": "aabbbbbbbabdchdb",
    "currentTime": "2026-05-24 14:30:00"
  },
  {
    "id": "b7e2d3a1-91cd-4f22-c4d8-3g5500000002",
    "input": "aa_",
    "currentTime": "2026-05-24 14:31:05"
  }
]
```

---

## Branch Workflow

This project uses feature branches for isolated development:

```bash
# Create a feature branch
git checkout -b feature/--86exp5ubg---initializing-rest-Api

# Stage specific files and commit with concise messages (1-5 words)
git add src/main/java/com/oraclequantapi/oraclequantapi/controller/Controller.java
git commit -m "Add Controller scaffold"

# Push to remote and open a Pull Request
git push origin feature/--86exp5ubg---initializing-rest-Api
```

All changes are committed in small, atomic commits and merged into `main` via Pull Request on GitHub.
