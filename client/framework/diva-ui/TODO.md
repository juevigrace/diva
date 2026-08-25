# diva-ui TODO

## Completed

- [x] Port master sources from `master:kmp/framework/diva-ui/` into `client/framework/diva-ui/src/`
- [x] Merge split packages (components.navigation → navigation, components.toaster → toast, etc.)
- [x] Navigator: non-generic NavKey, Option-based BackStack.current, launchSingleTop, pop returns Boolean
- [x] Screen: single scaffold with optional drawer (two overloads, no Option-lambda hack)
- [x] WindowUtils → WindowInfo (single data class, no expect/actual)
- [x] Toaster: instance-based class, rememberToasterState(), one palette expression
- [x] ObserveFlow: fix LaunchedEffect key bug
- [x] Delete Alias.kt
- [x] DateFilter → diva-core (pure, Compose-free, injectable Clock, explicit TimeZone)
- [x] toSelectableDates() adapter in diva-ui
- [x] Platform actuals: SystemUI (android/ios/jvm/js/wasmJs)
- [x] Compile-verify all targets (jvm, js, wasmJs, iosArm64, iosSimulatorArm64, android)
- [x] Tests: Navigator (12), NavBarState (5), DateFilter (10) — 27 green
- [x] Phase 2A: DefaultNavBarState, StateLayout/ScreenState, BackHandler expect/actual
- [x] Phase 2B: DialogController/DialogHost, popWithResult, navigateForResult
- [x] Phase 2C: dynamic color (DivaThemeConfig.useDynamicColors, expect/actual dynamicColorScheme)

---

## Pending

### 1. Navigator saved-state (config change / process death)

**Problem:** `rememberNavigator(start)` uses plain `remember {}`. On Android config
rotation or process death, the entire back stack resets to the start destination.
Master had the same flaw.

**nav3 1.1.1 API (already wired via lib catalog):**

```kotlin
// android overload — uses reflection, no SerializersModule needed
@Composable
fun rememberNavBackStack(vararg elements: NavKey): NavBackStack<NavKey>

// multiplatform overload — requires SavedStateConfiguration
@Composable
fun rememberNavBackStack(
    configuration: SavedStateConfiguration,
    vararg elements: NavKey,
): NavBackStack<NavKey>
```

`NavBackStack<T>` is a `SnapshotStateList<T>` wrapper (`MutableList<T>`).
`NavBackStackSerializer` handles serialization via kotlinx-serialization
polymorphic + `rememberSerializable`.

**Implementation options (pick one):**

**Option A — Delegate Navigator to NavBackStack internally.**
Keep the public `Navigator` class as-is (StateFlow-based, hoistable to VMs)
but internally back it with `NavBackStack` from `rememberNavBackStack`.
The `rememberNavigator` composable would create a `Navigator` whose
`mutBackStack` mirrors a `NavBackStack` kept via `rememberNavBackStack`.

- Pro: saves/restores automatically, battle-tested by nav3.
- Con: requires a `SavedStateConfiguration` in multiplatform (apps must
  register their sealed NavKey subtypes). Android overload auto-discovers.
- Con: Navigator's `StateFlow` can't directly wrap a SnapshotStateList —
  need a coroutine to observe changes and emit into the StateFlow, or
  abandon StateFlow in favor of exposing `NavBackStack` directly.

**Option B — kotlinx-serialization Saver.**
Write a `listSaver<Navigator, String>` that serializes each NavKey to JSON
via `Json.encodeToString(PolymorphicSerializer(NavKey::class), key)` and
restores by deserializing. Save into Compose's `rememberSaveable`.

- Pro: no nav3 coupling for saving mechanism.
- Con: must provide a `SerializersModule` anyway (same requirement as option A).
- Con: every list mutation triggers re-serialization on every recomposition
  (snapshot state changes → saver runs). Could be throttled but complex.

**Option C — ViewModel + SavedStateHandle.**
`Navigator` lives in a `DivaViewModel` subclass; saved via
`SavedStateHandle.set("backstack", json)`.

- Pro: survives process death and config change.
- Con: ties navigation state to VM lifetime (no composition-scoped nav
  without VM). Multiple NavHosts in one screen share one VM awkwardly.

**Recommended: Option A.** It's what nav3 is designed for, and the
`SavedStateConfiguration` requirement is a one-time app-level setup.
For Android specifically the reflection overload needs zero config.

**Steps:**

1. Add `SavedStateConfiguration` with polymorphic `NavKey` serializer
   in `DivaApp` (or a new `DivaNavConfiguration` composable) and provide
   it via composition local.
2. Refactor `rememberNavigator(start)` to internally create a `NavBackStack`
   via `rememberNavBackStack(config, start)` and synchronize it with the
   existing `Navigator` StateFlow (coroutine observer or snapshot listener).
3. Expose a way for VMs to create a `Navigator` that isn't saveable (plain
   `Navigator(start)` — unchanged) for testability and composition-local usage.
4. Add tests:
   - Verify `rememberNavigator` survives `createSavedStateRestore()` (Compose
     test API `ComposeTestRule.setContent + configurationChange`).
   - Verify non-saveable `Navigator(start)` path is unaffected.
5. Update `BackHandler` web actuals (blocked below).

**Files touched:** `navigation/Navigator.kt`, `DivaApp.kt` (or new config file),
`diva-ui-test` (new save-state test).

---

### 2. Web BackHandler actuals (js / wasmJs)

**Problem:** `BackHandler` expect/actual for js and wasmJs are currently
no-ops. Should wire into browser `history.pushState`/`popstate` to provide
a back gesture on web.

**Design:**

When `BackHandler(enabled=true, onBack=...)` is composed on web:
1. On mount (`LaunchedEffect(Unit)`), push an empty history entry:
   `window.history.pushState({}, "", null)` so the user has something
   to go "back" to.
2. Listen for `popstate` events. When the browser fires `popstate` (user
   hit browser back), call `onBack()`.
3. On dispose, remove the listener.
4. When `enabled` changes to `false`, temporarily remove the listener.
5. `onBack` should call `window.history.forward()` to cancel the
   browser's own state change, then invoke the real callback — otherwise
   the browser navigates away from the app.

**Implementation (jsMain and wasmJsMain, identical):**

```kotlin
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    val currentOnBack = rememberUpdatedState(onBack)
    LaunchedEffect(enabled) {
        if (!enabled) return@LaunchedEffect
        val listener: (Event) -> Unit = {
            currentOnBack.value.invoke()
        }
        window.addEventListener("popstate", listener)
        window.history.pushState(null, "", null)
        awaitCancellation()
    }
    DisposableEffect(enabled) {
        val handler: (Event) -> Unit = { currentOnBack.value.invoke() }
        if (enabled) {
            window.addEventListener("popstate", handler)
            window.history.pushState(null, "", null)
        }
        onDispose {
            window.removeEventListener("popstate", handler)
        }
    }
}
```

Wait — `LaunchedEffect` + `awaitCancellation` vs `DisposableEffect`:
use `DisposableEffect` only (cleaner lifecycle: push on enable, remove
on dispose). `LaunchedEffect` with `awaitCancellation` leaks the push
on recomposition changes.

**Refined approach (use `DisposableEffect` only):**

```kotlin
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    val currentOnBack = rememberUpdatedState(onBack)
    if (enabled) {
        DisposableEffect(Unit) {
            val handler: (Event) -> Unit = { currentOnBack.value.invoke() }
            window.addEventListener("popstate", handler)
            window.history.pushState(null, "", null)
            onDispose {
                window.removeEventListener("popstate", handler)
            }
        }
    }
}
```

- Push a history entry when enabled (gives the browser a "back" destination).
- Listen for `popstate` → calls `onBack`.
- Cleanup on dispose.
- When `enabled` becomes `false`, the `DisposableEffect` disposes, removes
  the listener. The extra history entry remains but harmless.

**Not handling:** the `popstate` consumer should call `window.history.forward()`
if they want to cancel the browser's actual navigation. This is a framework-level
convention; document it in the `BackHandler` KDoc.

**Files touched:** `src/jsMain/.../BackHandler.js.kt`, `src/wasmJsMain/.../BackHandler.wasmJs.kt`,
`src/commonMain/.../navigation/BackHandler.kt` (add KDoc noting web behavior).

---

### 3. Follow-up: NavHost integration with DialogHost

Currently `DialogHost` is standalone. If a dialog should be dismissable
via back gesture, wire `BackHandler(controller::dismiss)` inside
`DialogHost` when `controller.current.isSome`. Minor polish, no API change.

**Files touched:** `dialog/DialogHost.kt`.
