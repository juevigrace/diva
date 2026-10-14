# Rust + KMP Learning Plan for Diva

Goal: build core libraries in Rust (DB replacing SQLDelight + sqlc, server, CLI,
networking), share the same crates between backend and KMP client via UniFFI,
keep KMP/Compose only for platform-specific and UI glue.

## Where Rust slots into this repo

- `server/` (empty Go skeleton) → rewrite in Rust (axum)
- `cli/` (Makefile only) → rewrite in Rust (clap)
- `client/packages/shared/framework/diva-database*` (SQLDelight) → Rust `diva-db` crate
- `client/packages/shared/framework/diva-network` (Ktor) → Rust `diva-net` crate
- Future: new top-level `native/` cargo workspace, new `diva-native` KMP module
  wrapping UniFFI bindings

## Search terms

### Rust server
- `axum tutorial`
- `rust rest api axum sqlx postgres`
- `tokio tutorial rust`
- `actix-web vs axum` (optional alternative)

### Rust + KMP fusion
- `UniFFI Kotlin Multiplatform`
- `kotlin cinterop rust`
- `cargo-ndk android`

### Rust DB (replacing SQLDelight / sqlc)
- `rust sqlx tutorial postgres`
- `rusqlite tutorial`

### Rust web (js/wasmJs targets)
- `rust wasm-bindgen tutorial`

### Rust UI alternatives (later)
- `slint rust android ios`  ← best mobile story for a Compose alternative
- `dioxus mobile`
- `iced rust gui`
- `xilem rust`
- `egui`
- `gpui zed`

## What to learn first (ordered)

| # | Learn | Search | Replaces in diva |
|---|---|---|---|
| 1 | Rust basics: ownership, types, error handling, modules | `the rust programming language book`, `rustlings` | — |
| 2 | Cargo workspaces (multi-crate repos) | `cargo workspace tutorial` | future `native/` layout |
| 3 | Async Rust + tokio | `tokio tutorial rust` | foundation for server + sqlx |
| 4 | axum web server | `axum tutorial`, `rust rest api axum` | `server/` |
| 5 | sqlx (server, async) + rusqlite (client, sync) | `rust sqlx tutorial postgres`, `rusqlite tutorial` | SQLDelight + sqlc |
| 6 | CLI with clap | `rust clap tutorial` | `cli/` |
| 7 | FFI basics → UniFFI | `uniffi kotlin multiplatform`, `rust c interop extern c` | the KMP bridge |
| 8 | wasm-bindgen | `rust wasm-bindgen tutorial`, `wasm-pack` | web targets |
| 9 | Kotlin cinterop + JNI packaging via Gradle | `kotlin native cinterop rust`, `cargo-ndk android` | `diva-native` KMP module |

**Start with 1–4 only.** Build a tiny axum server with one POST/GET endpoint
backed by sqlite. That teaches most of what the real server needs.

## Notes

- **wasm-bindgen caveat:** it produces a JS package. Kotlin/JS consumes it fine;
  Kotlin/Wasm would call through JS glue (double-wasm overhead, untested).
  Keep a Kotlin fallback for wasmJs until proven.
- **UI:** don't start here. Slint is the only serious "Compose alternative in
  Rust" candidate for mobile; evaluate after DB/server are Rust.
- **Targets:** UniFFI covers android/ios/jvm/native; js/wasmJs need the
  wasm-bindgen path or Kotlin fallbacks.
