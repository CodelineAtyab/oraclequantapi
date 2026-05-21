# Deploy `oraclequantapi.jar` on Oracle Linux with Oracle Database

This guide assumes the JAR is already copied to the Oracle Linux server as:

```bash
/home/sulaiman/oraclequantapi.jar
```

The application will be reachable at:

```text
http://192.168.100.246:8080
```

Replace usernames, passwords, paths, and database connection details with your real values.

## Local and free setup

This guide is for a local/free setup:

- The Spring Boot app runs on your Oracle Linux machine.
- The database runs on the same Oracle Linux machine, using `localhost`.
- The app is accessed from your local network at `http://192.168.100.246:8080`.
- You do not need Oracle Cloud Infrastructure for this guide.
- You do not need a paid hosting service for this guide.
- Use Oracle Database Express Edition, also called Oracle XE, if you want a free Oracle Database for local development/testing.

Important: `192.168.100.246` is a private local network IP address. It works inside your home/lab network, but it is not a public internet deployment.

## 1. SSH into Oracle Linux

From your local machine:

```bash
ssh sulaiman@192.168.100.246
```

If you use a private key:

```bash
ssh -i /path/to/private-key sulaiman@192.168.100.246
```

Confirm the JAR exists:

```bash
ls -lh /home/sulaiman/oraclequantapi.jar
```

Because your terminal shows the file in your current home directory, this command should also work:

```bash
ls -lh ~/oraclequantapi.jar
```

## 2. Install Java 17

Spring Boot 3 requires Java 17 or newer.

```bash
sudo dnf install -y java-17-openjdk
java -version
```

Expected result should show Java 17.

## 3. Important build note for Oracle Database

The application must include the Oracle JDBC driver if you want to connect to Oracle Database.

In `pom.xml`, add this dependency before building the JAR:

```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <scope>runtime</scope>
</dependency>
```

Then rebuild locally:

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

Copy the rebuilt JAR to Oracle Linux again:

```bash
scp target/oraclequantapi-0.0.1-SNAPSHOT.jar sulaiman@192.168.100.246:/home/sulaiman/oraclequantapi.jar
```

If the JAR was built before adding the Oracle JDBC dependency, it may start with H2 but fail when configured for Oracle Database.

## 4. Create Oracle Database user/schema

Run these steps on the machine where Oracle Database is installed.

In your case, because you are deploying on Oracle Linux, start from the Oracle Linux terminal after SSH:

```bash
ssh sulaiman@192.168.100.246
```

You should see a prompt like this:

```text
[sulaiman@localhost ~]$
```

The `sqlplus` commands below run in the Oracle Linux terminal. The `CREATE USER` and `GRANT` commands run inside SQL*Plus after you connect.

First, check if SQL*Plus is installed:

```bash
sqlplus -v
```

If you see a SQL*Plus version, continue.

Connect to Oracle Database as an admin user. For Oracle XE, try:

```bash
sqlplus system@localhost:1521/XEPDB1
```

It will ask for the `system` password that was set when Oracle Database was installed.

If that does not work and you have sudo/root access on the database server, try:

```bash
sudo su - oracle
sqlplus / as sysdba
```

After connecting, your prompt will change to:

```text
SQL>
```

From this point, commands are SQL commands. Type them at the `SQL>` prompt.

If you connected using `sqlplus / as sysdba`, switch to the pluggable database:

```sql
ALTER SESSION SET CONTAINER = XEPDB1;
```

Create the application user. In Oracle, the user is also the schema.

```sql
CREATE USER ORACLEQUANTAPI IDENTIFIED BY "StrongPassword123";
```

Give the user permission to connect and create tables:

```sql
GRANT CREATE SESSION TO ORACLEQUANTAPI;
GRANT CREATE TABLE TO ORACLEQUANTAPI;
GRANT CREATE SEQUENCE TO ORACLEQUANTAPI;
GRANT CREATE VIEW TO ORACLEQUANTAPI;
GRANT UNLIMITED TABLESPACE TO ORACLEQUANTAPI;
```

Exit SQL*Plus:

```sql
EXIT;
```

Summary of where to type commands:

- Windows PowerShell: only use this for SSH or copying the JAR with `scp`
- Oracle Linux terminal: use this for `sqlplus`, `java -jar`, `systemctl`, and firewall commands
- SQL*Plus `SQL>` prompt: use this for `CREATE USER`, `GRANT`, `CREATE TABLE`, and `SELECT`

## 5. Create the application table

Still on Oracle Linux, connect as the new application user:

```bash
sqlplus ORACLEQUANTAPI/"StrongPassword123"@localhost:1521/XEPDB1
```

You should now see:

```text
SQL>
```

Create the table by typing this at the `SQL>` prompt:

```sql
CREATE TABLE sequence_history (
    id RAW(16) PRIMARY KEY,
    timestamp TIMESTAMP,
    input VARCHAR2(2000),
    output VARCHAR2(2000)
);
```

Check that the table exists:

```sql
SELECT table_name FROM user_tables WHERE table_name = 'SEQUENCE_HISTORY';
```

You should see:

```text
SEQUENCE_HISTORY
```

Exit SQL*Plus:

```sql
EXIT;
```

## 6. Configure the app for Oracle Database

You can configure Spring Boot from environment variables when starting the JAR.

Use this JDBC URL format:

```text
jdbc:oracle:thin:@//HOST:PORT/SERVICE_NAME
```

Example:

```bash
export SPRING_DATASOURCE_URL='jdbc:oracle:thin:@//localhost:1521/XEPDB1'
export SPRING_DATASOURCE_USERNAME='ORACLEQUANTAPI'
export SPRING_DATASOURCE_PASSWORD='StrongPassword123'
export SPRING_DATASOURCE_DRIVER_CLASS_NAME='oracle.jdbc.OracleDriver'
export SPRING_JPA_DATABASE_PLATFORM='org.hibernate.dialect.OracleDialect'
export SPRING_JPA_HIBERNATE_DDL_AUTO='validate'
export SPRING_JPA_OPEN_IN_VIEW='false'
```

Use `validate` after you create the schema manually. If you want Hibernate to create/update tables automatically during development, use:

```bash
export SPRING_JPA_HIBERNATE_DDL_AUTO='update'
```

For production, prefer `validate` and manage schema changes with SQL migrations.

## 7. Run the JAR manually

Start the application:

```bash
java -jar /home/sulaiman/oraclequantapi.jar
```

If you want it to listen specifically on port `8080`:

```bash
java -jar /home/sulaiman/oraclequantapi.jar --server.port=8080
```

Run it in the background:

```bash
nohup java -jar /home/sulaiman/oraclequantapi.jar --server.port=8080 > /home/sulaiman/oraclequantapi.log 2>&1 &
```

Check logs:

```bash
tail -f /home/sulaiman/oraclequantapi.log
```

Check the process:

```bash
ps -ef | grep oraclequantapi.jar
```

Stop the process:

```bash
pkill -f oraclequantapi.jar
```

## 8. Open port 8080 on Oracle Linux firewall

Check firewall status:

```bash
sudo firewall-cmd --state
```

Open port `8080`:

```bash
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
sudo firewall-cmd --list-ports
```

For this local/free setup, this is normally enough. You only need Oracle Cloud network rules if you later move the server to Oracle Cloud Infrastructure.

## 9. Test from the server

On the Oracle Linux server:

```bash
curl http://localhost:8080
```

Or test your API endpoints directly, for example:

```bash
curl http://localhost:8080/actuator/health
```

If actuator is not enabled, test one of your real controller endpoints instead.

## 10. Test from another machine

From your browser or terminal:

```text
http://192.168.100.246:8080
```

Using curl:

```bash
curl http://192.168.100.246:8080
```

If it works locally on the server but not from another machine, check:

- Oracle Linux firewall allows `8080/tcp`
- Your other machine is on the same local network as `192.168.100.246`
- The app is still running
- The IP address is correct
- No other process is using port `8080`

Cloud network security rules are only needed if this server is later deployed in a cloud VM. They are not required for the local/free setup described here.

Check port usage:

```bash
sudo ss -lntp | grep 8080
```

## 11. Run as a systemd service

Create a service file:

```bash
sudo vi /etc/systemd/system/oraclequantapi.service
```

Paste this:

```ini
[Unit]
Description=Oracle Quant API
After=network.target

[Service]
User=sulaiman
WorkingDirectory=/home/sulaiman
ExecStart=/usr/bin/java -jar /home/sulaiman/oraclequantapi.jar --server.port=8080
Restart=always
RestartSec=10

Environment=SPRING_DATASOURCE_URL=jdbc:oracle:thin:@//localhost:1521/XEPDB1
Environment=SPRING_DATASOURCE_USERNAME=ORACLEQUANTAPI
Environment=SPRING_DATASOURCE_PASSWORD=StrongPassword123
Environment=SPRING_DATASOURCE_DRIVER_CLASS_NAME=oracle.jdbc.OracleDriver
Environment=SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.OracleDialect
Environment=SPRING_JPA_HIBERNATE_DDL_AUTO=validate
Environment=SPRING_JPA_OPEN_IN_VIEW=false

[Install]
WantedBy=multi-user.target
```

Reload systemd:

```bash
sudo systemctl daemon-reload
```

Start the service:

```bash
sudo systemctl start oraclequantapi
```

Enable it on boot:

```bash
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

Restart after replacing the JAR:

```bash
sudo systemctl restart oraclequantapi
```

## 12. Common problems

### `ClassNotFoundException: oracle.jdbc.OracleDriver`

The JAR does not include the Oracle JDBC driver. Add `ojdbc11` to `pom.xml`, rebuild, and upload the new JAR.

### `ORA-01017: invalid username/password`

The database username or password is wrong. Test manually:

```bash
sqlplus ORACLEQUANTAPI/"StrongPassword123"@localhost:1521/XEPDB1
```

### `ORA-12514` or `ORA-12154`

The Oracle service name or connection URL is wrong. Check available services:

```bash
lsnrctl status
```

Use the service name shown by the listener, for example `XEPDB1`.

### App starts locally but browser cannot access it

Check that port `8080` is open:

```bash
sudo firewall-cmd --list-ports
sudo ss -lntp | grep 8080
```

Also check VM or cloud network rules.

### Port `8080` already in use

Find the process:

```bash
sudo ss -lntp | grep 8080
```

Run on a different port:

```bash
java -jar /home/sulaiman/oraclequantapi.jar --server.port=8081
```

Then access:

```text
http://192.168.100.246:8081
```
