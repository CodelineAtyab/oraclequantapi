# OracleQuant API Deployment Guide

This guide explains how to run **OracleQuant API** on **Oracle Linux VirtualBox** and connect it to an **Oracle Database Free Docker container running on Windows**.

This setup is for a **local development / training deployment**, not public production hosting.

---

## 1. Final Working Setup

| Part | Machine | Value |
|---|---|---|
| Oracle Database | Windows Docker | `container-registry.oracle.com/database/free:latest` |
| Oracle DB Container Name | Windows | `oracle-db` |
| Oracle DB Port | Windows | `1521` |
| Oracle DB Service Name | Docker Oracle DB | `FREEPDB1` |
| Oracle DB Admin User | Docker Oracle DB | `SYSTEM` |
| Oracle DB Admin Password | Docker Oracle DB | `OracleAdmin7191` |
| App DB User | Oracle DB | `ORACLEQUANTAPI` |
| App DB Password | Oracle DB | `StrongPassword123` |
| Spring Boot JAR | Oracle Linux | `/home/sulaiman/oraclequantapi.jar` |
| API Port | Oracle Linux | `8080` |
| Windows IP | Windows Wi-Fi | `172.20.10.2` |
| Oracle Linux IP | VirtualBox Bridged Adapter | `172.20.10.3` |
| API Base URL | Browser/Postman | `http://172.20.10.3:8080` |

> Important: Your IPs can change after restarting Wi-Fi, Windows hotspot, or the VM. Always check them again if something stops working.

---

## 2. Where to Run Each Command

| Command Type | Run It In |
|---|---|
| `docker ps`, `docker exec`, `docker logs` | Windows CMD |
| `netsh advfirewall ...` | Windows CMD as Administrator |
| `java -jar ...` | Oracle Linux terminal |
| `ip addr` | Oracle Linux terminal |
| `timeout 5 bash -c ...` | Oracle Linux terminal |
| SQL commands | Inside `SQL>` prompt |
| API testing | Windows browser or Postman |

---

## 3. Start Oracle Database on Windows

Open **Windows CMD**.

Check Docker:

```cmd
docker ps
```

If the container already exists but is stopped, start it:

```cmd
docker start oracle-db
```

If the container does not exist, create it:

```cmd
docker run -d --name oracle-db -p 1521:1521 -e ORACLE_PWD=OracleAdmin7191 container-registry.oracle.com/database/free:latest
```

Check that it is running:

```cmd
docker ps
```

Expected:

```text
oracle-db
0.0.0.0:1521->1521/tcp
healthy
```

Check logs:

```cmd
docker logs -f oracle-db
```

Wait until the database is ready. When done, press:

```text
CTRL + C
```

This only stops viewing logs. It does not stop the database.

---

## 4. Test Oracle Database Inside Docker

Run in **Windows CMD**:

```cmd
docker exec -it oracle-db sqlplus system/OracleAdmin7191@localhost:1521/FREEPDB1
```

If connected, you will see:

```sql
SQL>
```

Exit:

```sql
EXIT;
```

---

## 5. Open Windows Firewall Port 1521

Open **CMD as Administrator**.

Run:

```cmd
netsh advfirewall firewall add rule name="Oracle DB 1521" dir=in action=allow protocol=TCP localport=1521
```

Expected:

```text
Ok.
```

This allows Oracle Linux to reach the Oracle Database running on Windows Docker.

---

## 6. Configure VirtualBox Network

In **VirtualBox Manager**:

1. Shut down Oracle Linux first:

```bash
sudo poweroff
```

2. Select your Oracle Linux VM.
3. Go to:

```text
Settings -> Network
```

4. Adapter 1:

```text
Attached to: Bridged Adapter
Name: Intel(R) Dual Band Wireless-AC 3165
Cable Connected: checked
```

5. Click **OK**.
6. Start Oracle Linux again.

---

## 7. Check Oracle Linux IP

Run inside **Oracle Linux**:

```bash
ip addr
```

Look for `enp0s3`.

Working example:

```text
inet 172.20.10.3/28
```

So your Linux API IP is:

```text
172.20.10.3
```

Your Windows IP from `ipconfig` is:

```text
172.20.10.2
```

Both must be on the same network:

```text
Windows:      172.20.10.2
Oracle Linux: 172.20.10.3
```

---

## 8. Test Windows Oracle DB Port From Oracle Linux

Run this in **Oracle Linux**:

```bash
timeout 5 bash -c '</dev/tcp/172.20.10.2/1521' && echo "PORT OPEN" || echo "PORT CLOSED"
```

Expected:

```text
PORT OPEN
```

If it says `PORT CLOSED`, check:

1. Docker container is running on Windows:

```cmd
docker ps
```

2. Windows firewall rule exists.
3. VirtualBox network is set to Bridged Adapter.
4. Windows IP is still `172.20.10.2`.

---

## 9. Create Oracle App User

Run this in **Windows CMD**:

```cmd
docker exec -it oracle-db sqlplus system/OracleAdmin7191@localhost:1521/FREEPDB1
```

Inside `SQL>` run:

```sql
CREATE USER ORACLEQUANTAPI IDENTIFIED BY "StrongPassword123";

GRANT CREATE SESSION TO ORACLEQUANTAPI;
GRANT CREATE TABLE TO ORACLEQUANTAPI;
GRANT CREATE SEQUENCE TO ORACLEQUANTAPI;
GRANT CREATE VIEW TO ORACLEQUANTAPI;
GRANT UNLIMITED TABLESPACE TO ORACLEQUANTAPI;
```

If `CREATE USER` says the user already exists, continue with the grants.

Exit:

```sql
EXIT;
```

---

## 10. Create Application Table

Connect as the app user:

```cmd
docker exec -it oracle-db sqlplus ORACLEQUANTAPI/StrongPassword123@localhost:1521/FREEPDB1
```

Create the table:

```sql
CREATE TABLE sequence_history (
    id RAW(16) PRIMARY KEY,
    timestamp TIMESTAMP,
    input VARCHAR2(2000),
    output VARCHAR2(2000)
);
```

If it says table already exists, that is okay.

Check the table:

```sql
SELECT table_name
FROM user_tables
WHERE table_name = 'SEQUENCE_HISTORY';
```

Expected:

```text
SEQUENCE_HISTORY
```

Exit:

```sql
EXIT;
```

---

## 11. Copy JAR to Oracle Linux

If the JAR is not already on Oracle Linux, copy it from **Windows CMD or PowerShell**:

```cmd
scp "C:\Users\Codeline\Documents\GitHub\oraclequantapi\target\oraclequantapi-0.0.1.jar" sulaiman@172.20.10.3:/home/sulaiman/oraclequantapi.jar
```

Check on Oracle Linux:

```bash
ls -lh /home/sulaiman/oraclequantapi.jar
```

---

## 12. Check Java 17 on Oracle Linux

Run:

```bash
java -version
```

If Java is missing:

```bash
sudo dnf install -y java-17-openjdk
java -version
```

---

## 13. Run Spring Boot API Manually

Run this in **Oracle Linux**:

```bash
export SPRING_DATASOURCE_URL='jdbc:oracle:thin:@//172.20.10.2:1521/FREEPDB1'
export SPRING_DATASOURCE_USERNAME='ORACLEQUANTAPI'
export SPRING_DATASOURCE_PASSWORD='StrongPassword123'
export SPRING_DATASOURCE_DRIVER_CLASS_NAME='oracle.jdbc.OracleDriver'
export SPRING_JPA_HIBERNATE_DDL_AUTO='validate'
export SPRING_JPA_OPEN_IN_VIEW='false'

java -jar /home/sulaiman/oraclequantapi.jar --server.address=0.0.0.0 --server.port=8080
```

Successful startup should include something like:

```text
HikariPool-1 - Added connection
Tomcat started on port 8080
Started PackageMeasurementApiApplication
```

Keep this terminal open while testing.

---

## 14. Open Oracle Linux Firewall Port 8080

In another Oracle Linux terminal:

```bash
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
sudo firewall-cmd --list-ports
```

---

## 15. Test API From Windows

Open browser or Postman on Windows.

Base URL:

```text
http://172.20.10.3:8080
```

Test history:

```text
http://172.20.10.3:8080/history
```

Test conversion:

```text
http://172.20.10.3:8080/convert-measurements?input=1kg
```

---

## 16. Check Saved Data in Oracle Database

Run in **Windows CMD**:

```cmd
docker exec -it oracle-db sqlplus ORACLEQUANTAPI/StrongPassword123@localhost:1521/FREEPDB1
```

Inside `SQL>`:

```sql
SELECT * FROM sequence_history ORDER BY timestamp DESC;
```

Exit:

```sql
EXIT;
```

---

## 17. Run API in Background Temporarily

For quick background testing on Oracle Linux:

```bash
nohup java -jar /home/sulaiman/oraclequantapi.jar --server.address=0.0.0.0 --server.port=8080 > /home/sulaiman/oraclequantapi.log 2>&1 &
```

Check logs:

```bash
tail -f /home/sulaiman/oraclequantapi.log
```

Stop it:

```bash
pkill -f oraclequantapi.jar
```

---

## 18. Run API as a systemd Service

Create environment file:

```bash
sudo vi /etc/oraclequantapi.env
```

Paste:

```bash
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//172.20.10.2:1521/FREEPDB1
SPRING_DATASOURCE_USERNAME=ORACLEQUANTAPI
SPRING_DATASOURCE_PASSWORD=StrongPassword123
SPRING_DATASOURCE_DRIVER_CLASS_NAME=oracle.jdbc.OracleDriver
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_OPEN_IN_VIEW=false
```

Protect it:

```bash
sudo chmod 600 /etc/oraclequantapi.env
```

Create service file:

```bash
sudo vi /etc/systemd/system/oraclequantapi.service
```

Paste:

```ini
[Unit]
Description=Oracle Quant API
After=network.target

[Service]
User=sulaiman
WorkingDirectory=/home/sulaiman
EnvironmentFile=/etc/oraclequantapi.env
ExecStart=/usr/bin/java -jar /home/sulaiman/oraclequantapi.jar --server.address=0.0.0.0 --server.port=8080
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Start service:

```bash
sudo systemctl daemon-reload
sudo systemctl start oraclequantapi
sudo systemctl enable oraclequantapi
```

Check status:

```bash
sudo systemctl status oraclequantapi
```

View logs:

```bash
journalctl -u oraclequantapi -f
```

Restart after replacing JAR:

```bash
sudo systemctl restart oraclequantapi
```

Stop service:

```bash
sudo systemctl stop oraclequantapi
```

---

## 19. Daily Start Checklist

### On Windows

Start Docker Desktop.

Check Oracle DB:

```cmd
docker ps
```

If stopped:

```cmd
docker start oracle-db
```

### On Oracle Linux

Check IP:

```bash
ip addr
```

Test DB port:

```bash
timeout 5 bash -c '</dev/tcp/172.20.10.2/1521' && echo "PORT OPEN" || echo "PORT CLOSED"
```

Start API:

```bash
sudo systemctl start oraclequantapi
```

Check API:

```text
http://172.20.10.3:8080/history
```

---

## 20. Common Errors and Fixes

### Error: `No such container: oracle-db`

You are running Docker command in Oracle Linux, but the container is on Windows.

Run Docker commands from:

```text
C:\Users\Codeline>
```

Not from:

```text
[sulaiman@localhost ~]$
```

---

### Error: `NoRouteToHostException`

Linux cannot reach Windows DB port.

Fix:

1. Use Bridged Adapter in VirtualBox.
2. Make sure Windows and Linux are on same network.
3. Open Windows firewall port `1521`.
4. Test:

```bash
timeout 5 bash -c '</dev/tcp/172.20.10.2/1521' && echo "PORT OPEN" || echo "PORT CLOSED"
```

---

### Error: `Unknown host specified.: WINDOWS_IP`

You copied the placeholder `WINDOWS_IP`.

Wrong:

```text
jdbc:oracle:thin:@//WINDOWS_IP:1521/FREEPDB1
```

Correct:

```text
jdbc:oracle:thin:@//172.20.10.2:1521/FREEPDB1
```

---

### Error: `Schema-validation: missing table [sequence_history]`

The API connected to DB, but the table does not exist.

Create it:

```sql
CREATE TABLE sequence_history (
    id RAW(16) PRIMARY KEY,
    timestamp TIMESTAMP,
    input VARCHAR2(2000),
    output VARCHAR2(2000)
);
```

---

### Error: `New-NetFirewallRule is not recognized`

You ran a PowerShell command in CMD.

Either open PowerShell as Administrator, or use CMD as Administrator:

```cmd
netsh advfirewall firewall add rule name="Oracle DB 1521" dir=in action=allow protocol=TCP localport=1521
```

---

### Error: API not opening from Windows browser

Check if app is listening:

```bash
sudo ss -lntp | grep 8080
```

Open Linux firewall:

```bash
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

Make sure app is started with:

```text
--server.address=0.0.0.0
```

---

## 21. Important Notes

- This setup is local only.
- Do not expose these passwords publicly.
- For real production, change all passwords.
- Windows IP or Linux IP may change after restart.
- If IP changes, update:
    - `SPRING_DATASOURCE_URL`
    - API test URL
    - `/etc/oraclequantapi.env` if using systemd

---

## 22. Final Working URLs

API base:

```text
http://172.20.10.3:8080
```

History endpoint:

```text
http://172.20.10.3:8080/history
```

Convert endpoint:

```text
http://172.20.10.3:8080/convert-measurements?input=za
```

Oracle JDBC URL used by Spring Boot:

```text
jdbc:oracle:thin:@//172.20.10.2:1521/FREEPDB1
```
