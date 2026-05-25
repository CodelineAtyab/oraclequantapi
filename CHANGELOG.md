
---

#  CHANGELOG.md

```markdown id="changelog_final"
# CHANGELOG

---

## [1.0.0] - 2026-05-25

### Added
- Implemented full Sequence Processing REST API using Spring Boot.
- Added `/sequences/convert-measurements` endpoint for stream-based sequence computation.
- Introduced stateful streaming algorithm for dynamic grouping of input characters.
- Added Oracle Database persistence layer (SEQUENCE_HISTORY table).
- Implemented full history management (GET, PUT, DELETE operations).
- Added SLF4J logging for request tracing and debugging.
- Added input validation for allowed characters (a-z and underscore `_`).

### Changed
- Refactored architecture to a clean layered design (Controller → Service → Repository).
- Updated processing logic to support dynamic stream traversal instead of static parsing.
- Improved grouping logic to support runtime state transitions.

### Fixed
- Fixed schema mismatch issues between Oracle CLOB and entity mappings.
- Resolved Spring Boot ApplicationContext startup issues caused by schema validation conflicts.
- Fixed history persistence inconsistencies in output formatting.

---

## [0.2.0] - 2026-05-24

### Added
- Introduced sequence processing service layer.
- Added Oracle JPA repository integration.
- Enabled automatic persistence of conversion results into database.

---

## [0.1.0] - 2026-05-23

### Added
- Initial implementation of REST API.
- Basic sequence processing logic.
- Simple endpoint for conversion testing.

---

## [0.0.1] - 2026-05-21

### Added
- Project initialization using Spring Boot.
- Maven configuration setup.
- Base REST controller structure created.