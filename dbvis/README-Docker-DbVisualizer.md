# DbVisualizer → Docker Oracle (same DB as Spring Boot)

| Setting | Value |
|---------|--------|
| **URL** | `jdbc:oracle:thin:@//localhost:1521/XEPDB1` |
| **User** | `pkc_user` |
| **Password** | `PkcPassword1` |

**Table:** `MEASUREMENT_HISTORY` under schema `PKC_USER`

Start **Docker** and **Spring Boot** (`docker` profile) before connecting.

After `GET http://localhost:8081/convert-measurements?input=aa`, refresh the Data tab.
