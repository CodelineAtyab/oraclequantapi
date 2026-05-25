# Changelog

All notable changes to the Measurement Conversion API will be documented in this file.

---
## [1.0.0] - 2026-05-21
## [2.0.0] - 2026-05-25

### Added

* REST API for measurement sequence conversion (`/convert-measurements`)
* Oracle XE integration using Spring Data JPA
* Persistent history tracking in `CONVERSION_HISTORY` table
* Full History CRUD endpoints under `/input`
* Sequence parsing engine with counter-based tokenization
* Support for `'z'` chaining logic in decoding algorithm

### Changed

* Improved `SequenceService` logic for package decoding
* Refactored history update endpoints to correctly use `SequenceHistory`
* Enhanced logging for request tracing and debugging

### Fixed

* Fixed incorrect request body type in HistoryController (`SequenceHistory` instead of `Sequence`)
* Fixed update/patch logic consistency in `HistoryService`

### Security Notes

* Database credentials currently stored in `application.properties` (should be externalized in production)

---

## [1.0.0] - Initial Prototype

### Added

* Basic sequence parsing model
* In-memory sequence repository
* Initial REST controller structure

---

## Planned

* Input validation improvements
* Global exception handling (`@ControllerAdvice`)
* OpenAPI/Swagger documentation
* Docker support
* Authentication layer (JWT or Basic Auth)

---
