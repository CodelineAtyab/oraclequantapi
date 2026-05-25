-- =============================================================================
-- Run in Oracle SQL Developer as SYSTEM (password from your Oracle install)
-- Connection MUST use Service name: XEPDB1  (not just "OracleXE" CDB only)
-- =============================================================================
-- How to connect in SQL Developer:
--   1. New Connection → User: SYSTEM, Role: default
--   2. Connection Type: Basic → Host: localhost, Port: 1521
--   3. Service name: XEPDB1   (important!)
--   4. Test → Connect
--   5. Open this file → press F5 (Run Script) to run ALL lines
-- =============================================================================

ALTER SESSION SET CONTAINER = XEPDB1;

-- Remove old user if a previous attempt failed halfway
BEGIN
  EXECUTE IMMEDIATE 'DROP USER pkc_user CASCADE';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -1918 THEN
      RAISE;
    END IF;
END;
/

CREATE USER pkc_user IDENTIFIED BY "PkcPassword1"
  DEFAULT TABLESPACE USERS
  QUOTA UNLIMITED ON USERS;

GRANT CREATE SESSION TO pkc_user;
GRANT CONNECT TO pkc_user;
GRANT RESOURCE TO pkc_user;
GRANT CREATE TABLE TO pkc_user;
GRANT CREATE SEQUENCE TO pkc_user;

-- Verify (should show 1 row: OPEN)
SELECT username, account_status, default_tablespace
FROM dba_users
WHERE username = 'PKC_USER';

COMMIT;
