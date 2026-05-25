# CHANGELOG

## version [1.0.0] - 25-05-2026
### Fixed
- **IntelliJ Indexing and Project Structure Boundaries:** Resolved a local environment issue where Maven build lifecycle mappings were hidden and source files were displayed in monochromatic grey tones. Fixed by executing a targeted `Reload All Maven Projects` configuration cycle from the right sidebar tool window to re-index dependencies.
- **Maven Compilation and Context-Test Roadblocks:** Resolved a fatal test-phase runtime failure during the artifact packaging execution (`./mvnw clean package`) caused by disconnected context data layers.

## version [0.0.3] - 24-05-2026
### Added
- **Automated Data Auditing Layer:** Integrated automated database logging utilizing Spring Data JPA and Hibernate ORM to automatically persist every incoming sequence stream into the `SEQUENCE_HISTORY` table in real-time.
- **Project Documentation & Repository Guidelines:** Created a highly detailed `README.md` file including environment variables, prerequisites, and a complete step-by-step developer guide for the final Codeline submission branch validation and Pull Request (PR) workflow.

## version [0.0.2] - 23-05-2026
### Changed
- **Database Schema Auto-Synchronization:** Migrated datasource configurations in `application.properties` to utilize `spring.jpa.hibernate.ddl-auto=update` for dynamic Oracle table structuralization at startup, eliminating manual SQL setup scripts.
- **Context Testing Isolation Configuration:** Refactored `OraclequantapiApplicationTests.java` by injecting hardcoded connection configurations directly via `@SpringBootTest(properties = {...})` to isolate context evaluation boundaries from environmental container failures during compilation.

## version [0.0.1] - 21-05-2026
### Added
- **Initial Release of Measurement Conversion API:** Setup the baseline Spring Boot 3 enterprise structure using Java 17 and Maven project lifecycles.
- **Core Sequence Parser Logic:** Implemented the measurement parsing sequence engine to cleanly decode custom string streams into structured standard numeric arrays.
- **REST Controller Infrastructure:** Exposed a clean `SequenceController` component serving incoming web requests via the `GET /api/convert-measurements` endpoint.
- **Oracle Database Container Integration:** Setup a containerized Oracle Express Edition (XE) database engine running locally via Docker Desktop, mapped to standard database port `1521` with customized credential keys (`29999login`).
- **Live System Administration Mapping:** Established an active telemetry connection workspace inside DbVisualizer pointing to the local database schema to track data persistence and schema structures.