# Mac — Spring Boot + DbVisualizer (no Oracle Linux)

Your app stores data in a **file on your Mac**:

```
/Users/mac/IdeaProjects/oraclequantapi/data/pkcdb.mv.db
```

Table: **`MEASUREMENT_HISTORY`**

---

## 1) Start Spring Boot on Mac

**IntelliJ:** Run **Local Mac DbVisualizer**

**Terminal:**
```bash
cd /Users/mac/IdeaProjects/oraclequantapi
bash scripts/mac-start-local.sh
```

Or:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Wait for: `Started OraclequantapiApplication` and profile **`local`**.

---

## 2) Send test data (Postman or browser)

```
GET http://localhost:8081/convert-measurements?input=aa
GET http://localhost:8081/history
```

---

## 3) DbVisualizer connection

1. **Database** → **Create Database Connection**
2. **Driver:** H2
3. Settings:

| Field | Value |
|-------|--------|
| **Database URL** | `jdbc:h2:file:/Users/mac/IdeaProjects/oraclequantapi/data/pkcdb;AUTO_SERVER=TRUE` |
| **User** | `sa` |
| **Password** | *(empty)* |

4. **Connect** (while Spring Boot is running)

---

## 4) Open your table

```
Connection → PUBLIC → Tables → MEASUREMENT_HISTORY → Data → Refresh
```

SQL:
```sql
SELECT * FROM measurement_history ORDER BY id DESC;
```

After each Postman call, **Refresh** — new rows should appear.

---

## Linked?

| Step | OK? |
|------|-----|
| App runs with profile `local` | ☐ |
| `curl /history` shows data | ☐ |
| DbVisualizer connects to H2 URL above | ☐ |
| New row after API call + Refresh | ☐ |

All yes = Spring Boot and DbVisualizer share the same database on your Mac.
