# Mall Masters Visitor v0.1 acceptance matrix

## Works from source conceptually; CI verification pending
- Find fictitious POI by name or category (case-insensitive).
- Filter search results by floor and reset filters.
- Route from entrance to shop on different floor; use weighted paths and reject missing nodes.
- Keep tenant-specific data isolated once backend is implemented.
- Consent: friend sees location only when grant exists, not revoked, and unexpired.

## Must be tested before marking production-ready
- Android assemble + tests on CI.
- iOS compile on macOS, signing and TestFlight delivery.
- 3D renderer on real iPhone/Android (FPS, memory, startup time).
- Screen reader, dynamic text, reduce-motion, low-light contrast, rotation and small-screen layout.
- Graph: wheelchair-accessible routing; disconnected edges; emergency closures; changes of floor.
- Offline package versioning, downloads, checksums, migration and rollback.
- Session expiration and WSS reauthorization under revoked consent.
- Geofencing jitter, floor ambiguity, stale location, deduplication and notification rate limits.
- Multi-tenant access test across database, object store, Redis, WebSocket and workers.
- Backup restore and shared-VPS service impact / rollback.
- Real-world accuracy of location tracking; simulated floors are NOT suitable for actual routing.

## Release gates
R0: CI compiles Android, iOS and tests pass.
R1: Web demo deployed on HTTPS without breaking any existing domain path.
R2: Scanner creates actual geometric scans and supervised alignment.
R3: Explicit privacy consent and location accuracy validated by field trials.
