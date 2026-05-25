# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.0] - 2026-05-20

### Added
- **Core Conversion Engine**: Implemented `z*[^z]` decoder logic to convert measurement input strings into package inflow total lists. Supports standard characters (`a-y`), multiplier prefixes (`z`), empty/zero values (`_`), and case insensitivity.
- **Persistent REST History Endpoints**: Exposes standard endpoints to interact with history database tables:
  - `GET /convert-measurements` (accepts `input` / `convert-measurements` query parameter, converts, logs, and persists details).
  - `GET /history` (returns all records).
  - `GET /history/{id}` (returns a specific record).
  - `PUT /history/{id}` (replaces/updates record).
  - `PATCH /history/{id}` (patches specific fields).
  - `DELETE /history` (clears history record table).
- **Dual Persistence Architecture**: Setup standard H2 in-memory configuration by default (for out-of-the-box local testing) and added a dedicated `prod` profile using `ojdbc11` to persist history records on production Oracle XE databases.
- **7-day Rolling Log System**: Configured custom Logback rolling file appenders that generate structured daily logs under `logs/pkc-api.log` with a strict 7-day retention policy.
- **Full Automated Testing**: Added `ConversionEngineTest` to verify correctness on all requirements and edge cases.
