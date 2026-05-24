# CHANGELOG

## version [1.0.0] - 2026-05-24
### Added
- Integrated Oracle XE Pluggable Database (`XEPDB1`) via JDBC data source connections.
- Added explicit mapping layer configurations for `SEQUENCE_HISTORY` transaction records.
- Configured production SQL formatter property engines (`hibernate.format_sql`).

### Fixed
- Fixed critical `SchemaManagementException` initialization crash by routing the database URL context away from the root container and straight to the active pluggable container.

## version [0.2.0] - 2026-05-24
### Added
- Restored original entity schema structural bindings on the application core workspace module.
- Re-activated constructor-based dependency injections for the Spring Data JPA layer in `SequenceService`.
- Restored live business logic trace logging engines targeting native persistent system tables.

### Changed
- Re-enabled Spring Boot's global database and repository autoconfiguration layers (`DataSourceAutoConfiguration`, `HibernateJpaAutoConfiguration`).

## version [0.1.1] - 2026-05-24
### Fixed
- Fixed runtime class initialization crashes (`Cannot load driver class: oracle.jdbc.OracleDriver`) by adding missing `ojdbc11` runtime library engines to the project build system.
- Fixed broken code compilation blocks by pulling down correct `spring-boot-starter-data-jpa` dependencies via Maven synchronization passes.

## version [0.1.0] - 2026-05-24
### Added
- Implemented temporary pure-logic isolation testing parameters using Spring container auto-configuration exclusion blocks.
- Swapped active enterprise connection profiles for lightweight, decoupled, non-blocking code verification structures.
- Added inline stubs for relational record tracking logic blocks to allow quick endpoint evaluations via Postman.

## version [0.0.1] - 2026-05-21
### Added
- Initial deployment sprint of the `OraclequantapiApplication` built on Java 17 and Spring Boot.
- Configured REST endpoints (`/api/v1/convert-measurements`) for parsing encoded data streams.
- Established basic character grouping processing algorithms to handle weight total aggregations.