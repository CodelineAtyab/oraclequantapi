-- ===================================================================
--  MARYAM Measurement API - Oracle XE bootstrap (OPTIONAL)
--
--  The application can run directly against SYSTEM/1234 on XEPDB1; this
--  script is only required if you want a dedicated low-privilege user for
--  the application, which is the recommended production setup.
--
--  Run as SYSDBA:
--    sqlplus sys/1234@//localhost:1521/XEPDB1 as sysdba @oracle_xe_setup.sql
-- ===================================================================

ALTER SESSION SET CONTAINER = XEPDB1;

BEGIN
    EXECUTE IMMEDIATE 'DROP USER MARYAM_APP CASCADE';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE USER MARYAM_APP IDENTIFIED BY "1234"
    DEFAULT   TABLESPACE USERS
    TEMPORARY TABLESPACE TEMP
    QUOTA UNLIMITED ON USERS;

GRANT CONNECT, RESOURCE, CREATE SESSION, CREATE TABLE,
      CREATE SEQUENCE, CREATE VIEW TO MARYAM_APP;

CONNECT MARYAM_APP/"1234"@//localhost:1521/XEPDB1;

CREATE SEQUENCE MARYAM_CONV_HIST_SEQ START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE MARYAM_CONVERSION_HISTORY (
    ID            NUMBER(19)    PRIMARY KEY,
    REQUEST_TS    TIMESTAMP(6)  NOT NULL,
    SOURCE_IP     VARCHAR2(64)  NOT NULL,
    INPUT_VALUE   CLOB          NOT NULL,
    OUTPUT_VALUE  CLOB          NOT NULL
);

CREATE INDEX IDX_MARYAM_HIST_TS ON MARYAM_CONVERSION_HISTORY(REQUEST_TS);

COMMIT;
EXIT;
