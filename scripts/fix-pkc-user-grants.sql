-- Run as SYSTEM connected to XEPDB1 (F5 = Run Script)
-- Fixes: ORA-01045 user PKC_USER lacks CREATE SESSION privilege

ALTER SESSION SET CONTAINER = XEPDB1;

GRANT CREATE SESSION TO pkc_user;
GRANT CONNECT TO pkc_user;
GRANT RESOURCE TO pkc_user;
GRANT CREATE TABLE TO pkc_user;
GRANT CREATE SEQUENCE TO pkc_user;
GRANT UNLIMITED TABLESPACE TO pkc_user;

-- Must show OPEN
SELECT username, account_status FROM dba_users WHERE username = 'PKC_USER';

COMMIT;
