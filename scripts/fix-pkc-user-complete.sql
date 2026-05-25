-- =============================================================================
-- FIX ORA-01045 — run as SYSTEM in SQL Developer
-- Connection: Service name = XEPDB1  (or FREEPDB1 for Oracle 23c Free — see below)
-- Press F5 (Run Script) on the WHOLE file
-- =============================================================================

SELECT SYS_CONTEXT('USERENV', 'CON_NAME') AS current_container FROM DUAL;

ALTER SESSION SET CONTAINER = XEPDB1;

SELECT SYS_CONTEXT('USERENV', 'CON_NAME') AS after_switch FROM DUAL;

-- Clean start inside the pluggable database
BEGIN
  EXECUTE IMMEDIATE 'DROP USER pkc_user CASCADE';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE NOT IN (-1918, -01918) THEN
      RAISE;
    END IF;
END;
/

CREATE USER pkc_user IDENTIFIED BY PkcPassword1
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP
  QUOTA UNLIMITED ON USERS;

GRANT CREATE SESSION TO pkc_user;
GRANT CONNECT TO pkc_user;
GRANT RESOURCE TO pkc_user;
GRANT CREATE TABLE TO pkc_user;
GRANT CREATE SEQUENCE TO pkc_user;
GRANT UNLIMITED TABLESPACE TO pkc_user;

-- MUST return at least one row: CREATE SESSION
SELECT privilege
FROM dba_sys_privs
WHERE grantee = 'PKC_USER'
ORDER BY privilege;

SELECT username, account_status, default_tablespace
FROM dba_users
WHERE username = 'PKC_USER';

COMMIT;
