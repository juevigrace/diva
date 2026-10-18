# Diva — Rust + Kotlin Multiplatform Hybrid: Minimal Skeleton

> Branch: `feat/rust-kmp-skeleton` (cut from `reimagined-feat-ui-first-pass`).
> This document is the build guide for a **minimal** skeleton that proves the hybrid
> architecture end-to-end. It is not a migration plan for the full app; it is the smallest
> tree in which a Rust core is authored once and consumed from Android, Desktop (JVM), iOS
> and the web, alongside the existing KMP conventions.
>
> **Status: implemented and green on the JVM side.** See §9 for the verified build matrix.

---

## 1. Purpose

Prove, on a trimmed copy of the real project, that:

1. A **Rust workspace** can hold shared DTOs, DB helpers, an HTTP client and the backend
   server, with types defined **once** and shared between server and client.
2. A **KMP bridge** (`framework/diva-native-core` + `framework/diva-native`) can expose that
   Rust core to Kotlin on **JVM, Android and Kotlin/Native (iOS)** using
   **`ch.ubique.uniffi.plugin`** (UniFFI 0.32) — no hand-written JNI or cinterop.
3. **`js`/`wasmJs` targets keep compiling** via a pure-Kotlin `expect`/`actual` fallback, so
   the web pages that consume the KMP JS build output keep working, and Compose-for-web
   remains possible for downstream apps.
4. The **app entry points** (Android, Desktop, iOS) and the **web pages** build in exactly
   the same shape as today — only their dependencies are reduced.

### What this skeleton deliberately drops

- All existing domain features (media, player, library, auth, session, …), their databases
  and their SQLDelight schemas, `diva-network` (Ktor), `diva-database*` — all deleted on
  this branch. `git log` on `reimagined-feat-ui-first-pass` retains everything.
- `watchos`, `tvos`, `macos`, `linuxX64/Arm64`, `mingwX64` KMP targets. The bridge module
  publishes **`jvm`, `android`, `iosArm64`, `iosSimulatorArm64`, `js`, `wasmJs`** only.
- The Go `server/` directory (replaced by `rust/crates/diva-server`). The Go `cli/` is left
  untouched.

---

## 2. Architecture

```
┌───────────────────────────── rust/ (cargo workspace) ─────────────────────────────┐
│                                                                                    │
│  diva-types                diva-native                         diva-server          │
│  ───────────               ───────────                         ───────────          │
│  serde DTOs                uniffi exports              ┌───►  axum routes           │
│  (SystemInfo, Pong)        local SystemInfo record     │      serde_json           │
│  + Pong::ok/healthy        rusqlite (bundled)          │      uses diva-types      │
│                            reqwest (rustls)            │                           │
│                            crate-type =                │                           │
│                              lib/cdylib/staticlib   ───┘  (server does NOT link     │
│  ── diva-types is NOT linked into the cdylib ──          diva-native)               │
└──────────────────────────┬─────────────────────────────────────────────────────────┘
                           │ cdylib (JNI)          staticlib (cinterop klib)
                           ▼                            ▼
┌──────────── client/packages/shared/framework/diva-native-core (KMP library) ────────┐
│  applies `divabuild.library-rust` → library-base (jvm/android/ios, NO web)           │
│  + ch.ubique.uniffi.plugin; `uniffi { generateFromLibrary { packageName } }`         │
│  `cargo { packageDirectory = ../../../../../rust/crates/diva-native }`               │
│  → generated Kotlin bindings in package io.github.juevigrace.diva.native            │
└──────────────────────────┬──────────────────────────────────────────────────────────┘
                           │ (jvm/android/ios source sets depend on :diva-native-core)
                           ▼
┌──────────── client/packages/shared/framework/diva-native (KMP wrapper) ─────────────┐
│  applies `divabuild.framework-base`; adds js + wasmJs library binaries               │
│  package io.github.juevigrace.diva.native.api                                        │
│  commonMain : interface NativeCoreApi + expect fun createNativeCore() + models      │
│  jvmMain / androidMain / iosMain : actual → generated UniFFI bindings               │
│  jsMain / wasmJsMain             : actual → pure Kotlin stub (webNativeCore)        │
└──────────────────────────┬──────────────────────────────────────────────────────────┘
                           │
        ┌──────────────────┴───────────────────┐
        ▼                                      ▼
 app-lib/features/system/system-core   (framework/diva-core, diva-ui — kept)
  SystemRepository → NativeCoreApi
        │
        ▼
 apps/diva/shared-ui (App()) [:shared-ui]
        │
        ├── platforms/androidApp   (com.diva.app, DivaApp → App())
        ├── platforms/desktopApp   (com.diva.app.MainKt → App())
        ├── platforms/iosApp       (Xcode shell → Shared.framework → App())
        └── platforms/webApp/{landing-page, admin-page}  (Astro; consume diva-ui + KMP JS npm pkgs)
```

Key rule preserved from the current project: **the web pages never talk to Rust directly.**
They consume the KMP JS build output (`build/js/packages/*` via pnpm workspace) exactly as
today. Rust is reached through the per-platform `actual` implementations; on `js`/`wasmJs`
that `actual` is pure Kotlin.

Why two modules instead of one: the Ubique plugin **cannot generate for `js`/`wasmJs`**
(it fails with `Unhandled target`). So the plugin lives only on `diva-native-core`
(native/jvm/android), and `diva-native` is a thin platform-agnostic wrapper that adds the
web targets.

---

## 3. Target tree

```
diva/
├── PLAN-rust-kmp-minimal.md
├── rust/
│   ├── Cargo.toml                            # members: diva-types, diva-native,
│   ├── rust-toolchain.toml                   #   diva-server, uniffi-bindgen
│   └── crates/
│       ├── diva-types/{Cargo.toml, src/lib.rs}      # serde-only DTOs
│       ├── diva-native/{Cargo.toml, uniffi.toml, src/lib.rs}
│       ├── diva-server/{Cargo.toml, src/main.rs}
│       └── uniffi-bindgen/{Cargo.toml, src/main.rs} # manual bindgen runner
├── cli/                                      # untouched (Go)
└── client/
    ├── settings.gradle.kts                   # unchanged (3 composite builds)
    ├── gradle/libs.versions.toml             # + diva-native, diva-lib-system-core,
    │                                         #   uniffi plugin 1.3.0
    ├── gradle.properties                     # + kotlin.mpp.enableCInteropCommonization
    ├── build-logic/
    │   ├── build.gradle.kts                  # + libs.uniffi.gradle.plugin
    │   └── src/main/kotlin/
    │       ├── divabuild.kmp.gradle.kts
    │       ├── divabuild.targets-mobile.gradle.kts
    │       ├── divabuild.targets-web.gradle.kts
    │       ├── divabuild.library-base.gradle.kts
    │       ├── divabuild.library-rust.gradle.kts      # NEW — library-base + uniffi
    │       ├── divabuild.library-app-shared.gradle.kts
    │       ├── divabuild.library-app.gradle.kts
    │       ├── divabuild.library-app-ui.gradle.kts
    │       ├── divabuild.app-lib.gradle.kts
    │       ├── divabuild.app-lib-ui.gradle.kts
    │       ├── divabuild.framework-base.gradle.kts
    │       ├── divabuild.framework-ui.gradle.kts
    │       ├── divabuild.diva-app.gradle.kts
    │       ├── divabuild.diva-app-ui.gradle.kts
    │       ├── divabuild.app-android.gradle.kts
    │       ├── divabuild.app-desktop.gradle.kts
    │       ├── divabuild.compose.gradle.kts
    │       ├── divabuild.serialization.gradle.kts
    │       └── internal/*.kt
    └── packages/
        ├── shared/
        │   ├── framework/
        │   │   ├── settings.gradle.kts     # diva-core, diva-ui, diva-native,
        │   │   │                           #   diva-native-core
        │   │   ├── diva-core/              # kept
        │   │   ├── diva-ui/                # kept (Compose kit)
        │   │   ├── diva-native-core/       # NEW — UniFFI bindings (rust targets)
        │   │   └── diva-native/            # NEW — expect/actual wrapper (+ web)
        │   └── app-lib/
        │       ├── settings.gradle.kts     # core, ui, resources,
        │       │                           #   features:system:system-core
        │       ├── core/                   # kept
        │       ├── ui/                     # kept
        │       ├── resources/              # kept (ui depends on it)
        │       └── features/system/system-core/   # NEW — sample feature
        └── web/diva-ui/                    # untouched (shadcn web kit)
    └── apps/diva/
        ├── settings.gradle.kts             # :core, :ui, :resources, :shared-ui,
        │                                   #   platforms, + diva-lib-* substitutions
        ├── build.gradle.kts                # unchanged
        ├── core/                           # kept, trimmed
        ├── ui/                             # kept (DI module only)
        ├── resources/                      # kept (ui depends on it)
        ├── shared-ui/                      # rewritten minimal App()
        └── platforms/{androidApp,desktopApp,iosApp,webApp}   # kept
```

Deleted on this branch: `client/packages/shared/framework/{diva-database*,
diva-network*, diva-ui-test}`, `client/packages/shared/app-lib/{database,features/*}`
(except the new `features/system`), `client/apps/diva/{database,features/*}`,
`client/apps/diva/ui/.../components/*`, `server/`, and the obsolete build-logic
conventions `divabuild.{framework,framework-jvm,jvm,targets-native,test}.gradle.kts`.

---

## 4. Prerequisites

| Tool | Version | Notes |
|---|---|---|
| Rust | **1.95.0** | pinned in `rust/rust-toolchain.toml`; `rustup default 1.95.0` |
| Rust targets | `x86_64-unknown-linux-gnu`, `aarch64-linux-android`, `x86_64-linux-android` | Apple targets only on macOS |
| Android NDK | any recent r2x | needed for the Android cargo build |
| JDK | 21 | matches project |
| Gradle | ≥ **9.6.1** | project uses 9.8.0 (Ubique plugin requirement) |
| Kotlin | ≥ **2.4.0** | project uses 2.4.20 |
| AGP | 9.x | project uses 9.4.1 |
| UniFFI | **exactly 0.32.0** | `uniffi = "=0.32.0"` — required by plugin 1.3.0 |
| Node/pnpm | 22 / 11 | web pages |
| Xcode | 16+ | **macOS only** — iOS build cannot be verified on Linux |

```bash
rustup toolchain install 1.95.0
rustup default 1.95.0
rustup target add aarch64-linux-android x86_64-linux-android
# macOS only:
rustup target add aarch64-apple-ios aarch64-apple-ios-sim aarch64-apple-darwin
```

Kotlin/Native ignores targets that the host cannot build, so on Linux the iOS targets are
skipped; that is expected (the `uniffi-cinterop` task for `iosSimulatorArm64` emits a benign
warning).

---

## 5. Part A — Rust workspace

### A.1 `rust/Cargo.toml`

```toml
[workspace]
resolver = "2"
members = ["crates/diva-types", "crates/diva-native", "crates/diva-server", "crates/uniffi-bindgen"]

[workspace.package]
version = "0.1.0"
edition = "2021"

[workspace.dependencies]
serde = { version = "1", features = ["derive"] }
serde_json = "1"
uniffi = "=0.32.0"          # exact — matches Ubique plugin 1.3.0
thiserror = "2"
```

### A.2 `rust/rust-toolchain.toml`

```toml
[toolchain]
channel = "1.95.0"
components = ["rustfmt", "clippy"]
```

### A.3 `rust/crates/diva-types` — server-side DTOs (serde only)

```toml
[package]
name = "diva-types"
version.workspace = true
edition.workspace = true

[dependencies]
serde = { workspace = true }
```

```rust
// src/lib.rs
use serde::{Deserialize, Serialize};

/// Shared runtime information.
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct SystemInfo {
    pub os: String,
    pub arch: String,
    pub rust_version: String,
    pub core_version: String,
}

/// Shared envelope for the `/api/ping` and `/api/health` routes.
#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
pub struct Pong {
    pub status: String,
    pub service: String,
    pub version: String,
}

impl Pong {
    pub fn ok() -> Self { /* status:"ok", service:"diva-server", version:env!(...) */ }
    pub fn healthy() -> Self { /* status:"healthy", ... */ }
}
```

> **Design note — why no `uniffi` here.** The first attempt marked these as
> `uniffi::Record` and had `diva-native` link `diva-types`, relying on UniFFI *external
> types*. Library-mode bindgen failed across crate boundaries, so the type is now defined
> **serde-only** for the server, and `diva-native` declares its **own** `SystemInfo`
> `uniffi::Record` for the FFI surface. The two structs are intentionally identical in
> shape; keeping them separate is the pragmatic choice for the skeleton.

### A.4 `rust/crates/diva-native` — the KMP-facing core

```toml
[package]
name = "diva-native"
version.workspace = true
edition.workspace = true

[lib]
name = "diva_native"
crate-type = ["lib", "cdylib", "staticlib"]

[dependencies]
uniffi = { workspace = true }
thiserror = { workspace = true }
rusqlite = { version = "0.32", features = ["bundled"] }
reqwest = { version = "0.12", default-features = false, features = ["blocking", "json", "rustls-tls"] }
serde_json = { workspace = true }
```

`uniffi.toml`:

```toml
[bindings.kotlin]
package_name = "io.github.juevigrace.diva.native"
cdylib_name = "diva_native"
```

```rust
// src/lib.rs
uniffi::setup_scaffolding!();

/// Runtime information surfaced to Kotlin.
#[derive(Debug, Clone, PartialEq, Eq, uniffi::Record)]
pub struct SystemInfo {
    pub os: String,
    pub arch: String,
    pub rust_version: String,
    pub core_version: String,
}

/// Error type surfaced to Kotlin as an exception.
/// NOTE: fields are `detail`, not `message` — two variants with a field literally named
/// `message` produce "Conflicting declarations: val message" in the generated Kotlin.
#[derive(Debug, thiserror::Error, uniffi::Error)]
pub enum NativeError {
    #[error("http error: {detail}")]
    Http { detail: String },
    #[error("db error: {detail}")]
    Db { detail: String },
}

#[uniffi::export]
pub fn core_version() -> String { env!("CARGO_PKG_VERSION").to_string() }

#[uniffi::export]
pub fn system_info() -> SystemInfo {
    SystemInfo {
        os: std::env::consts::OS.to_string(),
        arch: std::env::consts::ARCH.to_string(),
        rust_version: "1.95".to_string(),
        core_version: core_version(),
    }
}

/// DB helper demo: bundled SQLite, create + insert + read back.
#[uniffi::export]
pub fn sqlite_roundtrip() -> Result<String, NativeError> {
    let conn = rusqlite::Connection::open_in_memory()
        .map_err(|e| NativeError::Db { detail: e.to_string() })?;
    conn.execute_batch(
        "CREATE TABLE probe (id INTEGER PRIMARY KEY, value TEXT NOT NULL);
         INSERT INTO probe (value) VALUES ('diva');",
    )
    .map_err(|e| NativeError::Db { detail: e.to_string() })?;
    let value: String = conn
        .query_row("SELECT value FROM probe WHERE id = 1", [], |row| row.get(0))
        .map_err(|e| NativeError::Db { detail: e.to_string() })?;
    Ok(format!("sqlite {} -> {}", rusqlite::version(), value))
}

/// HTTP helper demo: rustls, no OpenSSL, safe cross-compilation.
#[uniffi::export]
pub fn http_get(url: String) -> Result<String, NativeError> {
    let response =
        reqwest::blocking::get(&url).map_err(|e| NativeError::Http { detail: e.to_string() })?;
    response
        .text()
        .map_err(|e| NativeError::Http { detail: e.to_string() })
}
```

> **Async note.** Exports are synchronous (`reqwest::blocking`) so the skeleton ships without
> UniFFI's async scheduler. The Kotlin side calls them directly. The first follow-up is
> converting these to `#[uniffi::export] async fn` + `suspend`.

### A.5 `rust/crates/diva-server` — axum, sharing the types crate

```toml
[package]
name = "diva-server"
version.workspace = true
edition.workspace = true

[dependencies]
diva-types = { path = "../diva-types" }
axum = "0.8"
tokio = { version = "1", features = ["rt-multi-thread", "macros", "net"] }
serde_json = { workspace = true }
tracing = "0.1"
tracing-subscriber = "0.3"
```

`src/main.rs` exposes `GET /api/ping` and `GET /api/health`, both returning `Json<Pong>`
(`Pong::ok()` / `Pong::healthy()`), bound to `0.0.0.0:8080`.

### A.6 `rust/crates/uniffi-bindgen`

A tiny binary crate (`uniffi-bindgen` with `uniffi = { features = ["cli"] }`) used to run
binding generation by hand for inspection/CI. The Ubique Gradle plugin normally installs and
invokes its own bindgen; this crate is only a convenience.

### A.7 Rust verification

```bash
cargo build               # whole workspace (incl. diva-server)
cargo build -p diva-native
cargo run -p diva-server &   # then:
curl -s localhost:8080/api/ping
# {"status":"ok","service":"diva-server","version":"0.1.0"}
```

---

## 6. Part B — KMP client restructure

### B.1 Build-logic

Two required pieces:

1. **`diva-core` (and `diva-ui`) apply `divabuild.framework-base`** — the native/desktop/web
   set without the extra Apple/linux/mingw targets. `divabuild.targets-native.gradle.kts`,
   `divabuild.framework.gradle.kts`, `divabuild.framework-jvm.gradle.kts`,
   `divabuild.jvm.gradle.kts` and `divabuild.test.gradle.kts` are deleted.
2. **New `divabuild.library-rust.gradle.kts`** — applied by the UniFFI bridge module:

   ```kotlin
   plugins {
       id("divabuild.library-base")   // kmp library: jvm + android + ios (no web)
       id("ch.ubique.uniffi.plugin")
   }
   ```

   `build-logic/build.gradle.kts` adds `implementation(libs.uniffi.gradle.plugin)`.

### B.2 Version catalog additions (`client/gradle/libs.versions.toml`)

```toml
[versions]
uniffi-plugin = "1.3.0"

[plugins]
uniffi = { id = "ch.ubique.uniffi.plugin", version.ref = "uniffi-plugin" }

[libraries]
uniffi-gradle-plugin = { module = "ch.ubique.uniffi.plugin:ch.ubique.uniffi.plugin.gradle.plugin", version.ref = "uniffi-plugin" }
diva-native = { group = "io.github.juevigrace", name = "diva-native", version.ref = "diva-framework" }
diva-lib-system-core = { group = "io.github.juevigrace", name = "diva-lib-system-core", version.ref = "diva-lib" }
```

### B.3 `gradle.properties` (client root, framework, app-lib, apps/diva)

```properties
kotlin.mpp.enableCInteropCommonization=true
```

Required by Ubique: the generated native bindings reference cinterop declarations that are
only visible from a shared source set after the commonizer runs.

### B.4 `framework/diva-native-core` — UniFFI bindings (rust targets only)

```kotlin
plugins {
    id("divabuild.library-rust")
}

uniffi {
    generateFromLibrary {
        packageName = "io.github.juevigrace.diva.native"
    }
}

cargo {
    packageDirectory = layout.projectDirectory.dir("../../../../../rust/crates/diva-native")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.diva.native.core"
    }
}
```

Generated Kotlin lands in `commonMain` (expect) + `jvmMain`/`androidMain`/`nativeMain`
(actual), package `io.github.juevigrace.diva.native`, exposing top-level functions
`coreVersion`, `systemInfo`, `sqliteRoundtrip`, `httpGet` and the `SystemInfo` record.

### B.5 `framework/diva-native` — the multiplatform wrapper

```kotlin
@file:OptIn(ExperimentalWasmDsl::class)
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins { id("divabuild.framework-base") }

kotlin {
    js { browser(); nodejs(); binaries.library() }
    wasmJs { browser(); binaries.library() }

    sourceSets {
        jvmMain.dependencies { implementation(projects.divaNativeCore) }
        androidMain.dependencies { implementation(projects.divaNativeCore) }
        iosMain.dependencies { implementation(projects.divaNativeCore) }
        jvmTest.dependencies { implementation(kotlin("test")) }
    }
}
```

Source layout (package `io.github.juevigrace.diva.native.api`):

```
framework/diva-native/src/
├── commonMain/.../api/NativeCore.kt          # interface + expect factory + models + web stub
├── jvmMain/.../api/NativeCore.jvm.kt
├── androidMain/.../api/NativeCore.android.kt
├── iosMain/.../api/NativeCore.ios.kt
├── jsMain/.../api/NativeCore.js.kt           # actual → webNativeCore()
├── wasmJsMain/.../api/NativeCore.wasmJs.kt   # actual → webNativeCore()
└── jvmTest/.../api/NativeCoreTest.kt
```

`commonMain`:

```kotlin
package io.github.juevigrace.diva.native.api

interface NativeCoreApi {
    fun version(): String
    fun info(): NativeSystemInfo
    fun dbProbe(): String
    fun fetch(url: String): String
}

data class NativeSystemInfo(
    val os: String, val arch: String, val rustVersion: String, val coreVersion: String,
)

class NativeCoreException(message: String, cause: Throwable? = null) : Exception(message, cause)

expect fun createNativeCore(): NativeCoreApi

private class WebNativeCore : NativeCoreApi {
    private fun unsupported(): Nothing =
        throw NativeCoreException("The Rust core is not available on this target")
    override fun version(): String = unsupported()
    override fun info(): NativeSystemInfo = unsupported()
    override fun dbProbe(): String = unsupported()
    override fun fetch(url: String): String = unsupported()
}

internal fun webNativeCore(): NativeCoreApi = WebNativeCore()
```

`jvmMain` / `androidMain` / `iosMain` are identical in shape — they import the generated
`io.github.juevigrace.diva.native.{coreVersion,systemInfo,sqliteRoundtrip,httpGet}`, map
`SystemInfo` → `NativeSystemInfo`, and wrap failures in `NativeCoreException`:

```kotlin
actual fun createNativeCore(): NativeCoreApi = JvmNativeCore()

private class JvmNativeCore : NativeCoreApi {
    override fun version(): String = coreVersion()
    override fun info(): NativeSystemInfo {
        val info = systemInfo()
        return NativeSystemInfo(info.os, info.arch, info.rustVersion, info.coreVersion)
    }
    override fun dbProbe(): String =
        try { sqliteRoundtrip() }
        catch (t: Exception) { throw NativeCoreException(t.message ?: "sqlite probe failed", t) }
    override fun fetch(url: String): String =
        try { httpGet(url) }
        catch (t: Exception) { throw NativeCoreException(t.message ?: "http request failed", t) }
}
```

`jsMain` / `wasmJsMain`:

```kotlin
actual fun createNativeCore(): NativeCoreApi = webNativeCore()
```

`jvmTest` (`NativeCoreTest`) loads the cdylib via JNA and asserts `version()`, `info().os`
and `dbProbe().startsWith("sqlite")`.

### B.6 `app-lib/features/system/system-core` — the sample feature

`build.gradle.kts` uses `divabuild.app-lib`, adds `js`/`wasmJs` library binaries, and exposes
the wrapper as an API dependency:

```kotlin
plugins { id("divabuild.app-lib") }

kotlin {
    js { browser(); nodejs(); binaries.library() }
    wasmJs { browser(); nodejs(); binaries.library() }
    sourceSets { commonMain.dependencies { api(libs.diva.native) } }
}
```

Sources (package `io.github.juevigrace.diva.lib.system`):

```
├── domain/SystemRepository.kt      # interface
├── data/DefaultSystemRepository.kt # delegates to NativeCoreApi
└── di/SystemModule.kt              # fun systemModule(): Module
```

```kotlin
interface SystemRepository {
    fun version(): String
    fun info(): NativeSystemInfo
    fun runDatabaseProbe(): String
    fun fetch(url: String): String
}

fun systemModule(): Module = module {
    single<NativeCoreApi> { createNativeCore() }
    single<SystemRepository> { DefaultSystemRepository(get()) }
}
```

### B.7 `apps/diva` — trimmed app

- `shared-ui/App.kt` is a single Compose screen (`SystemScreen`) using `DivaTheme`
  (`DivaThemeConfig` + `lightScheme`/`darkScheme` + `AppTypography`) bound to a
  `koinViewModel()`-resolved `SystemViewModel`.
- `SystemViewModel` holds a `StateFlow<SystemUiState>` with `version`, `info`,
  `databaseProbe`, `httpResult`, `error`, and calls `SystemRepository` directly.
- `shared-ui/AppModule.kt` = `appModule()` with `includes(uiModule(), systemModule())` and a
  `viewModel { SystemViewModel(get()) }`.
- `apps/diva/ui` keeps only `ui/di/Module.kt` (`uiModule()` providing `DialogController` and
  `Toaster`).
- Entry points call no-arg `appModule()`: `androidApp/DivaApp.kt` (`Application`),
  `desktopApp/main.kt` (`application { startKoin { modules(appModule()) } }`),
  `shared-ui/iosMain/Main.kt` (`MainViewController`).
- `apps/diva/settings.gradle.kts` includes `:core`, `:ui`, `:resources`, `:shared-ui`, the
  two platforms, and substitutes `io.github.juevigrace:diva-lib-{core,ui,system-core}` →
  `:core`, `:ui`, `:features:system:system-core`.

---

## 7. Part C — Web packages

No restructuring. The contract is:

1. Every retained KMP library keeps `binaries.library()` + `generateTypeScriptDefinitions()`
   on `js`/`wasmJs`.
2. Kotlin emits npm packages under `**/build/js/packages/<name>`; the pnpm workspace glob
   `**/build/js/packages/*` picks them up.
3. `landing-page` / `admin-page` keep consuming `diva-ui` and `diva-lib-core`
   (`workspace:*`), plus any other KMP package with the matching `outputModuleName`.
4. The `js`/`wasmJs` `actual` for `diva-native` is pure Kotlin, so nothing in the web build
   requires the Rust toolchain — the web path stays buildable on any machine.

If Rust-on-web is ever wanted, it is a **later, isolated swap**: add a
`wasm32-unknown-unknown` cdylib + `wasm-bindgen`, publish its glue as an npm package, and
reimplement the two web `actual`s as `@JsModule` externals over it. Kotlin/Wasm can only
reach it through JS interop, so this only pays off for CPU-bound modules.

---

## 8. Part D — Server & CLI

- `server/` (Go) is deleted; `rust/crates/diva-server` replaces it and imports `diva-types`,
  so the JSON contract is defined once.
- `cli/` stays Go and is untouched.

---

## 9. Build & verification matrix

Verified green on this machine (Linux, JDK 21, Gradle 9.8.0):

| Target | Command | Status |
|---|---|---|
| Rust workspace | `cargo build` (in `rust/`) | ✅ |
| Server | `cargo run -p diva-server` + `curl /api/ping` | ✅ `{"status":"ok",...}` |
| Bindings + JVM runtime | `./gradlew :diva-native:jvmTest` (framework build) | ✅ JNA loads cdylib |
| JS library (bridge) | `./gradlew :diva-native:compileKotlinJs` | ✅ |
| app-lib feature | `./gradlew :features:system:system-core:compileKotlinJvm` | ✅ |
| app-lib core/ui | `./gradlew :core:compileKotlinJvm :ui:compileKotlinJvm` | ✅ |
| Desktop app | `./gradlew :platforms:desktopApp:compileKotlin` | ✅ |
| App core/ui/resources | `./gradlew :core:compileKotlinJvm :ui:compileKotlinJvm :resources:generateComposeResClass` | ✅ |
| Android | `./gradlew :platforms:androidApp:assembleDevelopmentDebug` | ⏳ not run (needs NDK) |
| Web pages | `pnpm -C client install && pnpm -C client -r build` | ⏳ not run |
| iOS | `./gradlew :shared-ui:linkDebugFrameworkIosSimulatorArm64` | ⏳ macOS only |

Ordered checklist:

1. `cargo build` + run server. ✅
2. Build `:diva-core`, `:diva-ui` (verify target trim). ✅
3. Build `:diva-native-core` + `:diva-native` for JVM (`jvmTest`). ✅
4. Build `:features:system:system-core`. ✅
5. Run desktop app — expect live Rust values. ⏳
6. Android assemble (NDK). ⏳
7. JS library build + `pnpm` build of pages.
8. iOS framework link on macOS.

---

## 10. CI sketch

```yaml
jobs:
  rust:
    runs-on: ubuntu-latest
    steps: [checkout, rustup (1.95.0 + android targets), cargo build, cargo test,
            cargo run server + curl health]
  client-linux:
    runs-on: ubuntu-latest
    steps: [checkout, setup-java 21, setup-gradle, android-ndk,
            ./gradlew :diva-native:jvmTest :platforms:androidApp:assembleDevelopmentDebug]
  client-macos:
    runs-on: macos-latest
    steps: [checkout, rustup + apple targets, ./gradlew :shared-ui:linkDebugFrameworkIosSimulatorArm64]
  web:
    runs-on: ubuntu-latest
    steps: [checkout, pnpm install, ./gradlew jsNodeProductionLibrary, pnpm -r build]
```

---

## 11. Risks & open questions

1. ~~**Ubique plugin DSL / binding location.**~~ **Resolved.** Plugin 1.3.0 works;
   bindings land in `commonMain` (expect) + per-target actuals. It cannot target
   `js`/`wasmJs`, hence the `diva-native-core` + `diva-native` split.
2. ~~**UniFFI version.**~~ **Resolved.** Must be pinned to exactly `=0.32.0`; the plugin's
   own bindgen version is tied to it.
3. ~~**External types.**~~ **Resolved.** Keep `diva-types` serde-only; declare FFI records
   locally in `diva-native`.
4. ~~**Generated Kotlin name clash.**~~ **Resolved.** `NativeError` fields named `detail`,
   not `message`.
5. **Configuration cache + `allWarningsAsErrors`.** The repo enables both; the plugin
   supports configuration cache, but note `org.gradle.configuration-cache.problems=warn`.
6. **No `Flow` across FFI.** DB observation from Rust needs callback interfaces wrapped in
   `callbackFlow`. The sample uses synchronous functions only.
7. **JVM native library packaging.** The generated JNI bindings must find the `.so` at
   runtime. `jvmTest` succeeds because the plugin wires the library path; packaging for a
   distributable jar still needs confirmation.
8. **iOS/Apple builds require macOS.** Only a cinterop warning surfaces on Linux.

---

## 12. Roadmap after the skeleton

1. Async UniFFI (`suspend` + `#[uniffi::export] async fn`) and `callbackFlow` for DB streams.
2. Port real DB helpers into `diva-native` (or a `diva-db` crate) with a single owner per DB.
3. Grow the shared API contract; server and client compile against it.
4. Media pipeline crate (`lofty`, `symphonia`, fingerprints) — the real performance win.
5. Optional `wasm-bindgen` swap for the web `actual`s of CPU-bound modules only.
6. Re-introduce the extra KMP targets (linux/mingw/watch/tv) as the space is needed.
```
