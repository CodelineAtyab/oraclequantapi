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
