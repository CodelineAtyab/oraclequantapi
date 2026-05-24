# Changelog

## v0.0.4 - Logging implementation
- Added `logback-spring.xml` with console and rolling file appender (7-day retention)
- Added SLF4J log statements to Controller, Service, and Converter

## v0.0.3 - Controller refactoring
- Fixed `OracleQuantController`: corrected endpoints to match REST spec (`/convert-measurements`, `@RequestParam`)
- Added service injection, source IP capture, and proper HTTP responses

## v0.0.2 - Converter fixes and service layer
- Fixed conversion algorithm: corrected `checkInput` inversion, `backageCount` reset, and post-loop flush
- Added `OracleQuantService` with history CRUD operations
- Added `OracleQuantRecord` JPA entity with id, timestamp, source_ip, input, output fields

## v0.0.1 - Initial Release
- Package Measurement Conversion endpoint (GET /convert-measurements)
- History endpoints: GET all, GET by id, PUT/PATCH update, DELETE clear
- Oracle XE database persistence for request history
- Input validation and conversion algorithm
- Logging with rolling file appender (7-day retention)
