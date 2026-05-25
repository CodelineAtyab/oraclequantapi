# Changelog

## [0.0.1-SNAPSHOT] - 2026-05-21
### Added
- Spring Boot 3.5 REST API with Oracle XE backend
- Package-measurement sequence conversion endpoint (`GET /convert-measurements`)
- History CRUD endpoints (`GET/PUT/PATCH/DELETE /history`)
- JPA entity `HistoryRecord` with auto-generated sequence IDs
- DTO `ConversionResponse` for API responses
- Rolling log appender with 7-day retention (`logs/pkc-api.log`)
- Sequence conversion algorithm supporting:
  - Count encoding via leading `z` (+26 each) + non-z terminator
  - Value reading with `_` as zero-value character
  - `z` value character contributing 27
- 8 test cases for conversion algorithm (all passing)
- Input validation with `@NotBlank` and `@Pattern(regexp = "[a-z_]+")`
- Global exception handler (`@RestControllerAdvice`) returning structured error JSON
- `ConversionResponse` DTO wrapping response in `{"packages": [...]}` format
