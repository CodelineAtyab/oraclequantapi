# Oracle Quant Sequence API

##  Overview
This is a Spring Boot REST API that processes input character sequences using a custom stateful streaming algorithm.

The system converts sequences into numeric package results, applies dynamic grouping logic based on control characters, and persists all transactions into an Oracle Database.

---

##  Core Concept
The algorithm processes input as a **stream of characters**, not static chunks.

- The character `z` acts as a **control/boundary modifier**
- The character `_` is treated as **neutral (value = 0)**
- Processing is based on **stateful traversal and lookahead accumulation**
- Groups are dynamically formed during runtime execution

---

##  Tech Stack
- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Oracle Database (XE / XEPDB1)
- SLF4J Logging
- Maven

---

##  Base URL


http://localhost:8080/sequences


---

##  API Endpoints

### 1. Convert Measurements
Processes a sequence and returns computed package totals.


GET /sequences/convert-measurements?input=abbcc


**Example Response**
```json
[2, 6]
2. Get All History
GET /sequences/history
3. Get History By ID
GET /sequences/history/{id}
4. Update History
PUT /sequences/history/{id}

Body

{
  "input": "abc",
  "output": "[2,6]",
  "sourceIpAddress": "127.0.0.1"
}
5. Delete All History
DELETE /sequences/history
6. Delete History By ID
DELETE /sequences/history/{id}
    # Algorithm Behavior
Input is processed as a continuous stream
z acts as a state modifier / boundary controller
_ contributes value 0 without breaking flow
Output is generated using dynamic grouping + lookahead accumulation
Final result is a list of computed package totals