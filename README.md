#Package Measurement Conversion API

## Features
- Convert measurement strings to numeric package totals.
- Persist conversion history in Oracle Database 21c Express Edition (XEPDB1) via Hibernate ORM.
- Configured to run as a native background Systemd service on an Oracle Linux Host.
- Logging system with real-time operational trace output directly to the console and system logs.

## Prerequisites
- Java 17 (OpenJDK) runtime environment.
- Oracle Database 21c XE (Express Edition) pluggable container instance active.
- Oracle Linux Host VM configured on Oracle VirtualBox.
- Apache Maven or included Maven wrapper configuration (mvnw).
- Docker & Docker Compose installed
- DB Visualizer 26.1.2

To compile the source code and assemble the executable standalone .jar production artifact, run the following command in your local host terminal:
# Using the Maven Wrapper script to bypass local system dependencies
.\mvnw clean package -DskipTests

The compiled output unit will be generated at:
- 📁 target/oraclequantapi-1.0.0.jar


# Execution & Deployment on Oracle Linux
1. Transfer Artifact:
   - Securely copy the production bundle from your host system to your Oracle Linux VM using an SCP tool or terminal shortcut:
     ```bash
      java -jar oraclequantapi-1.0.0.jar --spring.datasource.url=jdbc:oracle:thin:@//192.168.100.191:1521/XEPDB1
     ```
    
2. Configure background Systemd Service
   - To ensure the application runs continuously in production, package it into a system service file located at /etc/systemd/system/oracleapi.service:
   ```bash
    [Unit]
   Description=Package Measurement Conversion REST API Service
   After=syslog.target network.target
    
   [Service]
   User=mariya
   Group=mariya
   WorkingDirectory=/home/mariya/Documents
   ExecStart=/usr/bin/java -jar /home/mariya/Documents/oraclequantapi-1.0.0.jar
   SuccessExitStatus=143
   Restart=always
   RestartSec=10
    
   [Install]
   WantedBy=multi-user.target
   ```
3. Spin up the Service Engine
    ```bash
    # Refresh system configurations
    sudo systemctl daemon-reload
    
    # Enable and start the service thread background worker
    sudo systemctl enable oracleapi-service
    sudo systemctl start oracleapi-service
    ```


## API Endpoints
Convert Measurements

Method: GET

Endpoint: /api/v1/convert-measurements

Query Parameter: input=<string>

## Example Request
  ```bash
    GET http://localhost:8080/api/v1/convert-measurements?input=abbcc
   ```

## Response
```bash
    [2, 6]
   ```

## View Conversion Trace History
Method: GET

Endpoint: /api/v1/history

Target URL: http://localhost:8080/api/v1/history
