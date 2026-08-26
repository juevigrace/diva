# TODO: Root-Level Event Collection for DI Integration

## Problem

ViewModels need to trigger navigation, show toasts, and open dialogs — but they can't
hold a reference to `Navigator`, `Toaster`, or `DialogController` directly (they're
composable-scoped). The user wants to **register event collection once at the root**,
not per-screen/per-ViewModel.

### Current state

- `Navigator`, `Toaster`, `DialogController` are composable-scoped objects
- They're provided via composition locals (`LocalNavigator`, `LocalToaster`,
  `LocalDialogController`)
- ViewModels (`DivaViewModel`) have no access to composition locals

### Desired behavior

```kotlin
// ViewModel — emits events, no dependency on Navigator/Toaster/Dialog
class HomeViewModel : DivaViewModel() {
    fun goToProfile() { /* emit NavigationEvent.Navigate(ProfileKey) */ }
    fun showError() { /* emit ToastEvent("Something went wrong") */ }
}

// Root composable — registers collection ONCE, entire app gets it
DivaApp {
    NavHost(navigator, entryProvider) {
        // All screens can access LocalNavigator, LocalToaster, LocalDialogController
        // Event collection is automatic — no per-screen LaunchedEffect needed
    }
}
```

### Key constraint

The collection must be **root-level and automatic**. The user explicitly does NOT want:

```kotlin
// BAD — per-screen/per-ViewModel collection
@Composable
fun HomeScreen() {
    val navigator = LocalNavigator.currentOrThrow
    val viewModel = koinViewModel<HomeViewModel>()
    LaunchedEffect(Unit) {
        viewModel.navEvents.collect { navigator.handleEvents(flowOf(it)) }
    }
}
```

## Open questions

1. **Where does collection live?** Options:
   - `NavHost` collects Navigator events internally
   - `DivaApp` collects all three (Navigator, Toaster, Dialog) via a single
     `EventRegistry` composable
   - New `DivaEventHost` composable that wraps the app and collects all event flows

2. **How do ViewModels emit?** Options:
   - ViewModels hold `MutableSharedFlow<Event>` and expose it — composables register
     the flow with the root collector
   - ViewModels hold a lightweight `EventEmitter` interface (not the full controller)
     that writes to a shared flow
   - Single `EventBus` singleton registered in DI, ViewModels push events to it

3. **What's the event type?** Options:
   - Single sealed hierarchy: `sealed interface DivaEvent { Nav, Toast, Dialog }`
   - Separate flows per concern: `NavigationEvent`, `ToastEvent`, `DialogEvent`
   - Both — separate flows for type safety, unified `DivaEvent` wrapper for single
     collector

4. **Lifecycle?** Should event collection survive configuration changes?
   - Yes (use `LifecycleAware` or `remember` in root composable)
   - Tied to ViewModel scope (ViewModel dies = events stop)

5. **Multiple VMs?** If two ViewModels emit navigation events simultaneously, how are
   they ordered? First-come? Priority? Buffer?

## Possible solution sketch

```
DivaApp
  └─ EventCollector(navigator, toaster, dialogController)
       ├─ collects navigatorEvents: SharedFlow<NavigationEvent>
       ├─ collects toasterEvents: SharedFlow<ToastEvent>
       └─ collects dialogEvents: SharedFlow<DialogEvent>

ViewModels
  └─ hold EventEmitter (lightweight interface)
       └─ writes to a SharedFlow registered with EventCollector at root
```

The `EventCollector` composable would:
- Accept `Navigator`, `Toaster`, `DialogController` as parameters
- Collect their respective event flows via `LaunchedEffect`
- Be placed once in `DivaApp` or `NavHost`

ViewModels would:
- Receive `EventEmitter` (or individual emitters) via DI
- Call `emitter.emit(NavigationEvent.Navigate(key))`
- No knowledge of who collects or handles the events
