# Navigation TODOs

## Done

- [x] `TabNavigatorImpl` extends `BaseNavigator`, so inherited `navigate`/`pop`/`popUntil`/
      `replaceTop`/`replaceAll` drive the stack `TabNavHost` actually renders
- [x] `TabBackStack.selectedTab` derived from `tabHistory.last()` instead of stored, with
      `require(tabHistory.isNotEmpty())`
- [x] `BackStack.current` derived from `entries.last()` (non-null `NavKey`), with
      `require(entries.isNotEmpty())`; `Option` import dropped
- [x] `switchTo(tab, history)` collapsed to `switchTo(history)` — one arg that can't disagree
- [x] `restoreFrom` groups flat entries using tab roots as section delimiters
- [x] `TabNavigatorAlternative.kt` — option 3, fully commented, same class names inside the block
- [x] Tests: 25 passing (`NavigatorTest` 14, `TabNavigatorTest` 11)
- [x] `App.kt` `BackHandler` reads both the tab-level and tab-linear flows

## Blocking

- [ ] `HomeScreen.kt:96` — `when () {}` stub is a syntax error. Blocks `home-core`, and
      therefore `:shared-ui:compileKotlinJvm`. Finish or remove it.

## Restore-from-saved-state is dead code

Cause: `NavigationModule.kt:14-20` creates both navigators as Koin `single`s outside composition.
`rememberNavigator`/`rememberTabNavigator` have zero call sites, so `rememberNavBackStack` never
runs and `setEntries`/`restoreFrom` never fire. On process death navigation silently resets to Home.

- [ ] Decide: wire it up, or delete the unused machinery
- [ ] If wiring up: write the Koin singletons back into the platform `NavBackStack`
      (nothing currently writes it — it is only read via `toList()`)
- [ ] Guard the two-way sync against a feedback loop
- [ ] Guard the first-composition ordering hazard: the restored stack must be applied to the
      singleton *before* any write-back, or the fresh default overwrites the restored state
- [ ] Note `LocalSavedStateConfiguration` (`SavedState.kt:4`) defaults to `null`, so persistence
      is Android-only unless a parent provides it elsewhere

## Per-screen state is not persisted

`SavedStateHandle` appears nowhere in the codebase; `DivaViewModel` is a bare `ViewModel` with a
TODO. These all reset on process death:

- [ ] `FolderViewModel.currentFolderId` — the existing "never clobbered by a recomposition" comment
      is about the wrong failure mode; process death still loses it
- [ ] `SearchViewModel` query
- [ ] `PlayerViewModel` queue position / scrub offset
- [ ] `CollectionViewModel` form drafts
- [ ] Requires adding a `SavedStateHandle` constructor param to `DivaViewModel` + Koin wiring

Note: per-screen state is downstream of stack restoration. Entries must be recreated before the
ViewModels (and their handles) exist, so the stack layer has to work first.

## Robustness notes

- [ ] `switchTo` writes two independent flows (`tabBackStack.update` then `setEntries`) back to
      back. Correctness of the `App.kt` `enabled` condition relies on that adjacency — both are
      synchronous `MutableStateFlow` writes with no suspension between them, so the main thread
      never yields in between and Compose cannot observe the intermediate state. If `switchTo` is
      ever made to suspend or the two writes are separated, back handling silently breaks.
      Consider a comment at `switchTo` or on the `enabled` expression.
- [ ] Considered and skipped: `require(savedStacks.keys.none { it == selectedTab })`. Would catch
      a `switchTo` regression, but the KDoc already states the invariant and `switchTo` upholds it
      by construction.
- [ ] Optional: KDoc on `clearTabHistory` noting that other tabs' stacks stay parked and return
      via `selectTab`, so callers wanting to move tabs must call `selectTab` *before* clearing —
      otherwise the clear pins history and the later `selectTab` re-enables back out of Home.
      `clearTabHistory` currently has no production call sites.