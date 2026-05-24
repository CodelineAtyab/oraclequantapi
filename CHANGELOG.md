# Changelog

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
