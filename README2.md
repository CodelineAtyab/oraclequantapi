# oraclequantapi- Measurement Parser API

A Spring Boot REST API that parses encoded measurement strings and stores the history in an Oracle XE database.

---

## How to Run

1. Start Oracle XE:

run the docker start oracle-xe


2. Build the JAR:
-----
./mvnw clean package
-----
3. Run the app:
------
java -jar target/oraclequantapi-0.0.1.jar
------
4. Open the browser and test:
------
http://localhost:8080/convert-measurements?input=abbcc
------

......................

## How to Deploy to Oracle Linux VM

1. Copy the JAR to the VM:
-------
scp target/oraclequantapi-0.0.1 safa@<vm-ip>:~/
-------

2. SSH into the VM:
-------
ssh safa@<vm-ip>
-------

3. Run the app:
-------
java -jar ~/tryout-0.0.1-SNAPSHOT.jar
-------

4. Test from your browser:
-------
http://<vm-ip>:8080/convert-measurements?input=abbcc
-------

.......................

## How to Configure the Database

Open `src/main/resources/application.properties` and update these values:

```properties
spring.datasource.url=jdbc:oracle:thin:@<host>:1521/XEPDB1
spring.datasource.username=system
spring.datasource.password=<your-password>
```

- Replace `<host>` with `localhost` if running locally, or your Mac IP if running from the VM
- Replace `<your-password>` with the password you set when creating the Oracle XE container

.........................

## API Endpoints

| GET | `/convert-measurements?input=<string>` | Parse input and return result |
| GET | `/history` | Get all history records |
| GET | `/history/{id}` | Get one record by ID |
| PUT | `/history/{id}` | Update a record by ID |
| DELETE | `/history` | Clear all history records|

### Examples

**Convert:**
```
GET http://localhost:8080/convert-measurements?input=abbcc
Response: [2, 6]
```

**Get all history:**
```
GET http://localhost:8080/history
```

**Get one record:**
```
GET http://localhost:8080/history/1
```

**Update a record:**
```
PUT http://localhost:8080/history/1
Body: { "input": "abbcc" }
```

**Clear history:**
```
DELETE http://localhost:8080/history
Response: History has been Deleted
```

