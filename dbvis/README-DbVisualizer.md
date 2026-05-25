# DbVisualizer — connect to your program's data on Oracle Linux

Your API stores every conversion in **your own table**:

| Item | Value |
|------|--------|
| **Schema (owner)** | `PKC_USER` |
| **Table** | `MEASUREMENT_HISTORY` |
| **Columns** | `ID`, `TIMESTAMP`, `SOURCE_IP_ADDRESS`, `INPUT_VALUE`, `OUTPUT_VALUE` |

---

## Part 1 — On Oracle Linux VM (one time)

### 1. Run database setup

As **SYSTEM** on the VM (SQL*Plus or SQL Developer connected to **XEPDB1**):

```bash
sqlplus system@localhost:1521/XEPDB1
```

Then:

```sql
@/home/YOUR_USER/oraclequantapi/scripts/oracle-linux-setup.sql
```

(Or copy/paste the file contents.)

### 2. Open firewall for DbVisualizer (from your Mac)

```bash
sudo firewall-cmd --add-port=1521/tcp --permanent
sudo firewall-cmd --add-port=8081/tcp --permanent
sudo firewall-cmd --reload
```

### 3. Start the API on the VM

```bash
chmod +x scripts/start-pkc-api-oracle-linux.sh
./scripts/start-pkc-api-oracle-linux.sh /home/YOUR_USER/pkc-api.jar
```

Send a test request on the VM:

```bash
curl "http://localhost:8081/convert-measurements?input=aa"
```

---

## Part 2 — DbVisualizer on your Mac

### New connection

1. **Database** → **Create Database Connection**
2. **Name:** `OracleQuant PKC API (Linux VM)`
3. **Database type:** Oracle
4. **Driver:** Thin

| Field | Value |
|-------|--------|
| **Server** | Your VM IP (e.g. `192.168.56.101`) — **not** `localhost` |
| **Port** | `1521` |
| **Database (Service)** | `XEPDB1` |
| **Userid** | `pkc_user` |
| **Password** | `PkcPassword1` |

**Database URL:**

```text
jdbc:oracle:thin:@//YOUR_VM_IP:1521/XEPDB1
```

Replace `YOUR_VM_IP` with the Oracle Linux VM address from VirtualBox/VMware.

5. **Connect**

### Find your data

In the object tree:

```text
OracleQuant PKC API (Linux VM)
  └── PKC_USER
        └── Tables
              └── MEASUREMENT_HISTORY   ← double-click → Data tab → Refresh
```

### Sample query

```sql
SELECT id, timestamp, source_ip_address, input_value, output_value
FROM pkc_user.measurement_history
ORDER BY id DESC;
```

---

## If connection fails

| Error | Fix |
|-------|-----|
| Connection refused | Oracle listener not running on VM; open port 1521 |
| ORA-01017 | Wrong password — re-run `oracle-linux-setup.sql` |
| ORA-01045 | User missing grants — re-run `oracle-linux-setup.sql` |
| ORA-12514 | Wrong service — try `FREEPDB1` instead of `XEPDB1` |
| Table empty | Start `pkc-api.jar` with `oracle` profile and call the API |

---

## Oracle 23c Free on Linux

Use service **FREEPDB1** and run `scripts/fix-pkc-user-FREEPDB1.sql` instead, then:

```text
jdbc:oracle:thin:@//YOUR_VM_IP:1521/FREEPDB1
```
