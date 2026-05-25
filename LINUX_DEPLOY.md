# Oracle Linux deployment guide

This is the complete procedure that satisfies the "deployable on Oracle Linux"
requirement of the evaluation document.

---

## 0. Prerequisites on the Linux box

- Oracle Linux 8 or 9 (or any RHEL-family distribution).
- Network access to the public yum repos (for installing Java and Maven).
- Oracle XE 21c already installed, with:
  - the listener up on port `1521`
  - the pluggable database `XEPDB1` reachable
  - the `SYS` password set to `1234` (so it matches `application.properties`)
- An OS user with `sudo` rights for the install step.

Confirm Oracle is up before doing anything else:

```bash
lsnrctl status
sqlplus sys/1234@//localhost:1521/XEPDB1 as sysdba <<< "select 1 from dual;"
```

If both succeed you are ready.

---

## 1. Transfer the project zip from Windows

On Windows (only the first time):

```cmd
cd "C:\Users\walaa\JSON API\maryam-measurement-api"
make-zip.bat
```

`maryam-measurement-api.zip` is produced one folder up (`C:\Users\walaa\JSON API\maryam-measurement-api.zip`).

Copy it to the Linux box:

```cmd
scp "..\maryam-measurement-api.zip" maryam@<linux-host>:/tmp/
```

---

## 2. Unpack and run the installer on Linux

```bash
ssh maryam@<linux-host>
cd /tmp
unzip maryam-measurement-api.zip -d maryam-measurement-api
cd maryam-measurement-api
chmod +x deployment/linux-install.sh
./deployment/linux-install.sh
```

That single script performs every step:

1. Installs OpenJDK 17 and Maven via `dnf` (idempotent).
2. Creates the `maryam` OS user, `/opt/maryam-measurement-api`,
   `/var/log/maryam-measurement-api`.
3. Builds the jar with `mvn clean package`.
4. Creates the `MARYAM_APP` database user and schema by running
   `deployment/oracle_xe_setup.sql` against `XEPDB1`.
5. Installs `/etc/systemd/system/maryam-measurement-api.service`, then
   `systemctl daemon-reload && enable && restart`.
6. Runs the eight PDF smoke tests and prints `OK`/`FAIL` per case.

When it finishes you should see eight `OK` lines.

---

## 3. Switch the app to the dedicated user

The installer created `MARYAM_APP`. Edit
`/opt/maryam-measurement-api/application.properties`:

```properties
spring.datasource.username=MARYAM_APP
spring.datasource.password=1234
```

Restart:

```bash
sudo systemctl restart maryam-measurement-api
sudo systemctl status maryam-measurement-api
```

---

## 4. Verify the service

```bash
# health
curl http://localhost:8080/maryam/actuator/health
# version info
cat /opt/maryam-measurement-api/version.txt    # bundled inside the jar; see /v3/api-docs for the live value
# logs
sudo journalctl -u maryam-measurement-api -n 50 --no-pager
ls -lh /var/log/maryam-measurement-api/
```

---

## 5. The full PDF acceptance test

These are the exact requests the evaluator runs against the deployed service.
Run them on the Linux box (or from your laptop against `http://<linux-host>:8080/maryam`):

```bash
H=http://localhost:8080/maryam

# Conversion - eight PDF examples
curl  "$H/convert-measurements?input=aa"                                 # [1]
curl  "$H/convert-measurements?input=abbcc"                              # [2,6]
curl  "$H/convert-measurements?input=dz_a_aazzaaa"                       # [28,53,1]
curl  "$H/convert-measurements?input=a_"                                 # [0]
curl  "$H/convert-measurements?input=abcdabcdab"                         # [2,7,7]
curl  "$H/convert-measurements?input=abcdabcdab_"                        # [2,7,7,0]
curl  "$H/convert-measurements?input=zdaaaaaaaabaaaaaaaabaaaaaaaabbaa"   # [34]
curl  "$H/convert-measurements?input=za_a_a_a_a_a_a_a_a_a_a_a_a_azaaa"   # [40,1]

# History - CRUD
curl       "$H/history"                                                  # list
curl       "$H/history/1"                                                # one
curl -X PUT    "$H/history/1" \
     -H "Content-Type: application/json" \
     -d '{"input":"aa","output":"[1]"}'                                  # replace
curl -X PATCH  "$H/history/1" \
     -H "Content-Type: application/json" \
     -d '{"output":"[1]"}'                                               # partial
curl -X DELETE "$H/history/2"                                            # one
curl -X DELETE "$H/history"                                              # all

# Swagger UI
xdg-open http://localhost:8080/maryam/swagger-ui/index.html   ||  \
    echo "Open http://<linux-host>:8080/maryam/swagger-ui/index.html in a browser"
```

---

## 6. Verifying the database side

```bash
sqlplus maryam_app/1234@//localhost:1521/XEPDB1
```

```sql
SQL> SELECT id, request_ts, source_ip, SUBSTR(input_value,1,40) AS input,
            SUBSTR(output_value,1,40) AS output
       FROM maryam_conversion_history
       ORDER BY id;
```

This proves the data really lives in Oracle XE - one of the explicit evaluation
points.

---

## 7. Troubleshooting

| Symptom                                                    | Likely cause                              | Fix                                                                                       |
|------------------------------------------------------------|-------------------------------------------|-------------------------------------------------------------------------------------------|
| `ORA-12541: no listener`                                   | Listener not running                      | `sudo systemctl restart oracle-xe-21c` (or whichever unit Oracle XE installed)            |
| `ORA-01017: invalid username/password`                     | Password mismatch                         | Edit `/opt/maryam-measurement-api/application.properties` to the correct password           |
| Port 8080 already in use                                   | Another process bound                     | Change `server.port` in `application.properties`, then `systemctl restart`                |
| `Failed to start maryam-measurement-api.service`             | Java path wrong in the unit               | Edit `/etc/systemd/system/maryam-measurement-api.service`, fix `JAVA_HOME` and `ExecStart`  |
| `curl: (7) Failed to connect`                              | Firewall                                  | `sudo firewall-cmd --add-port=8080/tcp --permanent && sudo firewall-cmd --reload`         |

---

## 8. Uninstall

```bash
sudo systemctl stop maryam-measurement-api
sudo systemctl disable maryam-measurement-api
sudo rm /etc/systemd/system/maryam-measurement-api.service
sudo rm -rf /opt/maryam-measurement-api /var/log/maryam-measurement-api
sudo userdel -r maryam
```
