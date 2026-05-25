# My OracleQuant API notes

This is my small project for the evaluation.  
You send text like `aa` → you get `[1]`. It saves everything in **MEASUREMENT_HISTORY**.  
Jar: **pkc-api.jar** · runs on port **8081**

I am still learning so this readme is just my story day by day.

---

## Day 1 · Tuesday 20 May 2026

**What I tried to do**  
Start the task. Read the PDF. I did not understand everything at first (packages, letters a-z = numbers, etc).

**What I did**  
- Opened the project in IntelliJ  
- Made the conversion code (`ConversionEngine`)  
- Ran unit tests — the 8 examples from PDF passed (I was happy here)  
- Started Spring Boot with **H2** database (teacher said easier for first step)

**Challenges**  
- PDF is long and has many rules  
- I confused where to put my files in the package folder  
- First time using Spring Boot + Maven together

**What worked at end of day**  
Tests green. I could think the math part is OK.

---

## Day 2 · Wednesday 21 May 2026

**What I tried to do**  
Make the API real — controller, history save, try Postman.

**What I did**  
- Added `/convert-measurements` and `/history` endpoints  
- Every Postman call should save a row (input, output, IP, time)  
- Fixed a stupid mistake: class name must match file name (`PackageConversionApplication`)  
- Connected **DbVisualizer** to H2 file database  
- Saw table **MEASUREMENT_HISTORY** with my test data

**Challenges**  
- App would not compile sometimes (JPA package missing in IDE)  
- Had to run `mvn compile` to see real errors  
- Did not know JDBC URL for H2 at first

**What worked at end of day**  
Postman: `http://localhost:8081/convert-measurements?input=aa` → `[1]` and row in DbVisualizer.

---

## Day 3 · Thursday 22 May 2026

**What I tried to do**  
Move from H2 to **real Oracle** (because assignment wants Oracle XE).

**What I did**  
- Installed Oracle XE on Mac  
- Wrote SQL scripts for user **pkc_user** password **PkcPassword1**  
- Made `oracle` profile in `application.properties`  
- Tried DbVisualizer with `localhost:1521` and service **XEPDB1**

**Challenges**  
- **ORA-01017** — login failed (I typed wrong password many times)  
- **ORA-01045** — user exists but cannot connect (needed GRANT CREATE SESSION)  
- App started very slow or failed when Oracle was off  
- I did not understand SYS vs SYSTEM vs pkc_user at first

**What worked at end of day**  
User `pkc_user` can login in SQL*Plus / DbVisualizer. Table created.

---

## Day 4 · Friday 23 May 2026

**What I tried to do**  
Run the app on **Oracle Linux VM** (assignment also talks about Linux server).

**What I did**  
- VM name: **AlharithLinux**, user **alharith**  
- Built jar: `mvn clean package` → copied **pkc-api.jar** to `/home/alharith/`  
- Tried SSH from Mac (port 2222) — did not work, used VM screen instead  
- Ran `java -jar pkc-api.jar` on Linux

**Challenges**  
- I ran `pkc-api.out` once by mistake (that is not the app file)  
- Tried `mvn spring-boot:run` on VM → error **no plugin spring-boot** (no pom.xml there, only jar)  
- DbVisualizer from Mac to VM IP `10.0.2.15` → **timeout** (NAT network, I learned later)  
- Port 8081 sometimes already used from old run  
- Connection refused in Postman because app stuck on database

**What worked at end of day**  
I understand: on VM you only need `java -jar`, not Maven. Scripts saved in `scripts/` folder.

---

## Day 5 · Saturday 24 May 2026

**What I tried to do**  
Make everything work on **my Mac** with **Docker + DbVisualizer + Postman** (my main setup now).

**What I did**  
- Oracle in Docker container **oracle-xe**  
- Script `docker-setup-db-only.sh` for pkc_user  
- Script `mac-start-docker.sh` to start DB + API  
- API runs **inside Docker** on network **pkc-net** with profile **docker-internal**  
- DbVisualizer on Mac still uses `127.0.0.1:1521`

**Challenges**  
- Docker said container name already exists — had to `docker start oracle-xe` not create new one  
- `mvn spring-boot:run` with docker profile on Mac **hangs** forever (HikariPool) — big confusion  
- Postman **ECONNREFUSED** — app never really started  
- Port **8081 in use** — needed `scripts/stop-pkc-api.sh` first

**What worked at end of day (finally)**  
```bash
bash scripts/stop-pkc-api.sh
bash scripts/mac-start-docker.sh
```
Postman OK. DbVisualizer shows **MEASUREMENT_HISTORY**. I can show my trainer.

---

## Quick run (what I use now)

```bash
cd /Users/mac/IdeaProjects/oraclequantapi
bash scripts/stop-pkc-api.sh
bash scripts/mac-start-docker.sh
```

- Postman: `http://127.0.0.1:8081/convert-measurements?input=aa`  
- History: `http://127.0.0.1:8081/history`  
- DbVisualizer: `jdbc:oracle:thin:@//127.0.0.1:1521/XEPDB1` · user `pkc_user` · pass `PkcPassword1`

Build jar: `mvn clean package`

---

## Small hints (for me later)

- docker profile + mvn on Mac = bad (hangs)  
- use `.jar` not `.out`  
- stop old app before start (8081)  
- VM = `java -jar` only if no project folder

---

## Submit

branch `alharith-alsubhi-submission-branch` → push → PR → Slack trainer

*last update 24 May 2026*
