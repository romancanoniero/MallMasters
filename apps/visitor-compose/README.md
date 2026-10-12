# MallMasters Visitor

**Target:** Kotlin Multiplatform + Compose Multiplatform for iOS and Android.

This directory is reserved for the native shared UI and domain modules. No executable project or signed IPA is present yet.

First deliverables:
1. Gradle project and Kotlin Multiplatform iOS/Android targets.
2. Shared models (mall, floor, point of interest, route).
3. Compose screens for floor selection, search, and route preview.
4. Renderer abstraction; test embedded web/Three.js vs native engine.
5. iOS CI signed TestFlight build and Android APK.

Companion prototype: `../visitor-web/index.html` uses fictional layouts and store positions; ION Orchard is only a conceptual reference.
