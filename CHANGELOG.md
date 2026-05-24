# CHANGELOG

## version [1.0.0] - 2026-05-24
### Added
- Initial release of OracleQuant Package Measurement Conversion API.
- Core algorithm for parsing measurement strings (MeasurementParser.java).
- GET /api/convert-measurements endpoint for converting input strings.
- Support for multi-character z-prefix number encoding (e.g. "zza" = 53).
- Support for zero-sentinel '_' character (value = 0, terminates number).
- Persist all conversion requests to Oracle XE Database automatically.
- GET /history endpoint to fetch all history records.
- GET /history/{id} endpoint to fetch a specific history record.
- PUT /history/{id} endpoint for full update of a history record.
- PATCH /history/{id} endpoint for partial update of a history record.
- DELETE /history endpoint to clear all history records.
- HistoryRecord JPA entity with id, timestamp, sourceIpAddress, input, output.
- Spring Boot 3.2.0 with Oracle OpenJDK 17.
- Maven build system producing pkc-api.jar.
- Oracle JDBC driver (ojdbc11) for Oracle XE connection.
- Hibernate ORM with GenerationType.SEQUENCE for Oracle compatibility.
- Logging to console and logs/ directory with 7-day rolling files via Logback.
- README.md with setup, database config, and API documentation.
- CHANGELOG.md file to track project versions and changes.
- Deployed and running on Oracle Linux via SSH.
- Firewall port 8080 opened on Oracle Linux.
- Application configured as a background service via systemd.

### Fixed
- Fixed jakarta.persistence import (lowercase j) in HistoryRecord.java.
- Fixed GenerationType from IDENTITY to SEQUENCE for Oracle XE compatibility.
- Fixed MeasurementParser — added full parse() method with z-accumulation.
- Fixed ConversionController — removed HistoryRepository direct injection.
- Fixed return type from List<Integer> to List<Long> in ConversionService.
- Fixed HistoryService — added sourceIpAddress field to update method.
- Fixed HistoryController — added proper 404 response for missing records.
- Fixed application.properties — corrected Oracle dialect configuration.
- Fixed project JDK from OpenJDK 25 to OpenJDK 17 in IntelliJ.
- Fixed pom.xml — merged duplicate build sections, added finalName pkc-api.
- Fixed deployment — corrected application.properties path on Oracle Linux.
- Fixed datasource driver-class-name typo (deiver → driver) on Oracle Linux.