# Changelog

All notable project changes are documented in this file.

## 0.0.1-SNAPSHOT - 2026-05-21

- Created the initial Spring Boot Maven project.
- Added the basic application entry point.
- Added the initial README file.

## 0.0.2-SNAPSHOT - 2026-05-21

- Added the package measurement conversion REST API.
- Implemented conversion logic for `_`, `a-z`, and multi-character `z` values.
- Added request history persistence with `id`, `timestamp`, `source_ip_address`, `input`, and `output` fields.
- Added history REST endpoints for `GET`, `PUT`, `PATCH`, and `DELETE` requests.
- Added Oracle XE database configuration through the `oracle` Spring profile.
- Added local H2 database support for development and automated tests.
- Added Logback configuration for console logging and daily rolling file logging.
- Configured application logs to write to `logs/oraclequantapi.log` by default.
- Configured rolling log retention for seven days.
- Added unit and integration tests for conversion and API behavior.

## 0.0.3-SNAPSHOT - 2026-05-21

- Refactored the Java source structure into `controller`, `service`, `model`, and `repository` packages.
- Renamed the main application class to `OracleQuantApiApplication`.
- Renamed service and repository classes to match the requested project structure.
- Updated unit and integration tests to match the new package structure.

## 0.0.4-SNAPSHOT - 2026-05-22

- Updated conversion behavior so invalid characters are treated as `0` instead of returning errors.
- Updated conversion behavior so missing measured values are treated as `0`.
- Added support for empty or missing `input` values, returning an empty list.
- Updated history persistence to support empty request input values.
- Updated tests for tolerant conversion behavior.

## 0.0.5-SNAPSHOT - 2026-05-22

- Reviewed the current project structure and database-related files.
- Added `ConversionHistory` entity file for conversion history data modeling.
- Added `ConversionHistoryRepository` for conversion history database access.
- Kept the active API history flow using `HistoryRecord`, `HistoryRepository`, and `HistoryService`.
- Updated `CHANGELOG.md` and `version.txt` to match the current project state.

## 0.0.6-SNAPSHOT - 2026-05-23

- Updated logging documentation in `CHANGELOG.md` and `version.txt`.
- Documented that the application uses Logback for console and file logging.
- Documented that logs are written to `logs/oraclequantapi.log` by default.
- Documented that rolling log files are retained for seven days.