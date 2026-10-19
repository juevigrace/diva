# Branch review: `reimagined` -> `reimagined-feat-ui-first-pass`

File-level overview of everything this branch changes relative to `reimagined`.

| | |
|---|---|
| Base branch | `reimagined` @ `276c182a` |
| Head branch | `reimagined-feat-ui-first-pass` @ `30a889f2` |
| Merge base | `276c182a` |
| Relationship | base is a direct ancestor of head (linear, no rebase/merge divergence) |
| Commits | 28 |
| Files changed | 390 (106 new, 31 deleted, 246 modified, 7 renamed) |
| Net lines | +7790 / -2370 (plus 5 binary files) |

```
diff  git diff reimagined...reimagined-feat-ui-first-pass
```

> **Updated 2026-10-19 against head `30a889f2`.** The previous snapshot of this file covered head
> `20ab4b81` (5 commits). Note that this review file itself is committed on the branch
> (`14894d5c`) and is excluded from every number below.

## What changed since the previous snapshot

23 commits were added, and several of them **undo or supersede what the first snapshot
reported**. Read this section before trusting any earlier finding:

- **The `*Content.kt` refactor was reverted.** `FoldersContent.kt`, `ProfileContent.kt`,
  `LibraryContent.kt`, `SearchResultsContent.kt` (plus `FolderMock.kt`, `HomeMock.kt`,
  search-core's `SearchField.kt`) were all added at `20ab4b81` and deleted again at `4d9ce3e4`
  ("redo change from screens to content tabs"). Screens are back: `LibraryScreen.kt` and
  `ProfileScreen.kt` are modified in place, `SearchScreen.kt` was replaced by
  `SearchResultsScreen.kt`. Because the files cancel out, **none of this churn is visible in the
  cumulative diff**.
- **The whole `folder` feature is deleted and replaced by a new `file` feature** - the biggest
  new subsystem on the branch (device file system browsing, per-platform FS service actuals,
  Android storage permissions, SQLDelight entry storage).
- **`settings` moved from the shared `app-lib` into the app** (`features/settings/...` under
  `client/apps/diva`), with a `SharedSettings*` remnant left in `app-lib` and a brand-new
  `settings-database` module.
- **The tab navigation model was rewritten** - `TabNavigator.kt` heavily reworked,
  `TabNavHost.kt` deleted, `NavSurfaceState.kt` added, and a `TabNavigatorAlternative.kt`
  added that is **not referenced anywhere** (dead code as of head - see Review flags).
- **A multiplatform permission-requester framework** was added to `diva-core`
  (common expect + Android/iOS/JVM actuals), used by the new file feature.
- **A repository/API sweep** across `app-lib` features (user, session, devices, permissions,
  auth) and app features (media, collection, mix, playlist, server), plus a mechanical cleanup
  in `cf053880`: 80 files lose the same two lines (`@file:OptIn(ExperimentalJsExport::class)`
  and its import).
- The **feed module** was started and abandoned inside the branch (`adbcb16e` "undo feed module
  and home tab change") - 9 files that do not appear in the cumulative diff at all.

## Commits

- `30a889f2` remove test md
- `f25fb306` fix styling and start fixing performance for fs service and repo
- `61a9e8e0` fix android exception transform test, status bar overlap, add file table new fields
- `ff2444d9` fix file repo, ui works now
- `4f3ca29e` add permission requester, android storage permissions, refactor file storage and ui
- `62d106da` making fixes to query file
- `a7270b21` start refactor of folder module
- `48d1cd05` fix bars current styling
- `bffa189e` refactor profile logic
- `998647b7` fix artwork params, rail screen padding values
- `510a05e6` add carousel and artwork components refactor
- `adbcb16e` undo feed module and home tab change
- `b6276f86` fix navs and screen paths
- `ec317320` start refactor of ui design and useless components
- `56932497` fix repos
- `cb4c8710` add todo for repo fix
- `51a2b87f` fixing repos
- `5686e5ad` add first app settings implementations
- `cf053880` fix format and unused imports
- `afcf0483` refactor settings modules
- `4d9ce3e4` refactor style in screens, redo change from screens to content tabs
- `c3ee5b8d` add todo for navigation restore
- `14894d5c` reafactor tab navigator
- `20ab4b81` start refactor of tabs
- `2ac450f9` fixes
- `3938da95` fix carousel buttons and scroll
- `8773ba59` fix memory settings and date format error
- `41ab30ff` add first ui designs

## Biggest changes (review these first)

Ranked by total lines touched.

| | Churn | +/- | File |
|---|---:|---|---|
| mod | 338 | +325 / -13 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/screen/PlayerScreen.kt` |
| mod | 317 | +164 / -153 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/TabNavigator.kt` |
| mod | 296 | +217 / -79 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/screen/HomeScreen.kt` |
| mod | 276 | +169 / -107 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/screen/ProfileScreen.kt` |
| **new** | 268 | +268 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/TabNavigatorAlternative.kt` |
| **new** | 232 | +232 / -0 | `client/packages/shared/framework/diva-ui-test/src/commonTest/kotlin/io/github/juevigrace/diva/ui/test/TabNavigatorTest.kt` |
| **new** | 225 | +225 / -0 | `client/apps/diva/features/file/file-database/src/commonMain/kotlin/com/diva/app/file/database/EntryStorage.kt` |
| **new** | 219 | +219 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/ui/screen/FilesScreen.kt` |
| mod | 188 | +186 / -2 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/components/HomeContent.kt` |
| **new** | 183 | +183 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/screen/SearchResultsScreen.kt` |
| **new** | 170 | +170 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/Carousel.kt` |
| ~~del~~ | 169 | +0 / -169 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/kotlin/com/diva/app/folder/database/FolderStorage.kt`~~ |
| mod | 148 | +91 / -57 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/AdaptiveScreen.kt` |
| **new** | 134 | +134 / -0 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/presentation/viewmodel/AppViewModel.kt` |
| **new** | 130 | +130 / -0 | `client/apps/diva/features/file/file-core/src/androidMain/kotlin/com/diva/app/file/data/services/FileSystemServiceImpl.android.kt` |
| **new** | 124 | +124 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/viewmodel/FilesViewModel.kt` |
| **new** | 119 | +119 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/input/SearchField.kt` |
| **new** | 119 | +119 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchMock.kt` |
| **new** | 117 | +117 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/CarouselCard.kt` |
| mod | 111 | +94 / -17 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/screen/LibraryScreen.kt` |

## The recurring patterns

This is no longer one refactor - it is **several waves**, some of which cancel each other out.
Ordered by when they landed:

1. **Screens -> Content tabs, then back again** (`20ab4b81`, reverted at `4d9ce3e4`). The first
   snapshot's headline pattern - delete `FooScreen.kt` + `FooNav.kt`, add `FooContent.kt` +
   `FooMock.kt` - was applied and then **fully undone** a few commits later. Only the `*Mock.kt`
   files for library, player and search survived. Screens exist again at head; the Content
   composables do not. Net effect in the cumulative diff: almost nothing - but it explains why
   files like `FoldersContent.kt` do not appear in any table below.
2. **Tab navigation rewrite** (`14894d5c`, `ec317320`, `b6276f86`). `TabNavHost.kt` deleted,
   `TabNavigator.kt` rewritten in place (+164/-153), new `NavSurfaceState.kt` (drawer/scaffold
   state hoisting), new `Navigator` plumbing, layout screens (`RailScreen`,
   `ModalDrawerScreen`, `PermanentDrawerScreen`, `AdaptiveScreen`) reworked around it, and a
   232-line `TabNavigatorTest.kt` added. Alongside the rewrite sits a complete second
   implementation, `TabNavigatorAlternative.kt`, referenced by nothing.
3. **`folder` feature deleted, `file` feature created** (`a7270b21`..`f25fb306`). The entire
   folder module (-582 across 17 files, including its database and models modules) is removed
   from `settings.gradle.kts` and replaced by a new `file` feature: `file-core`,
   `file-models`, `file-database` (the latter two reuse the folder modules' Gradle files via
   rename). The new feature adds a real device-file-system integration - expect/actual
   `FileSystemService` (Android/iOS/JVM), a `StoragePermissionRequester`, and `EntryStorage`
   over SQLDelight.
4. **`settings` relocated and implemented** (`afcf0483`, `5686e5ad`). The settings feature moves
   from `client/packages/shared/app-lib/features/settings` into
   `client/apps/diva/features/settings`, gaining its own `settings-database` module
   (`SettingsStorageImpl`, `Settings.sq`, migration) and a `SettingsViewModel`. What remains in
   `app-lib` is renamed to `SharedSettingsRepository` / `SharedSettingsViewModel` /
   `SharedSettingsState`, and `AppSettings.kt` becomes `Settings.kt`.
5. **Permission framework in `diva-core`** (`4f3ca29e`). Common `PermissionRequester` expect +
   Android actual (with `ActivityProvider`, `ActivityLifecycleObserver`, manifest +
   App-Startup initializer), iOS/JVM stubs, and a `RequestPermission` composable in `diva-ui`.
   The file feature's Android storage-permission flow is built on it.
6. **Repository and model sweep** (`51a2b87f`, `56932497`, `cf053880`). Data/domain repository
   pairs refactored across user, session, devices, permissions, auth (app-lib) and media,
   collection, mix, playlist, server, library, search, player (app). Profile takes a different
   route: its repository is deleted outright and `ProfileViewModel` now consumes the shared
   `UserRepository.observeCurrent()`.
   Separately, **80 files carry the identical mechanical 2-line cleanup** (removal of
   `@file:OptIn(ExperimentalJsExport::class)` + its import) - safe to skim past.

## Feature modules (app)

### File - new module

37 files, +1664 / -8

The largest new subsystem. Replaces the deleted folder feature with device file-system
browsing: models (`FsEntry`, `MimeType`, `FileCategory`, `EntryMeta`, `Directory`,
`DeviceFile`), a SQLDelight store (`EntryStorage`, `Entry.sq`, `EntrySchema.sq`, `File.sq`,
migration), a domain `FileRepository` + impl, per-platform `FileSystemService` actuals, a
`StoragePermissionRequester` (common expect, Android real, iOS/JVM no-op), DI modules, and the
UI (`FilesScreen`, `FilesViewModel`, `DirectoryState`, `DirectoryEvents`, `FileNav`,
`Routes`).

| | +/- | File |
|---|---:|---|
| **new** | +225 / -0 | `client/apps/diva/features/file/file-database/src/commonMain/kotlin/com/diva/app/file/database/EntryStorage.kt` |
| **new** | +219 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/ui/screen/FilesScreen.kt` |
| **new** | +130 / -0 | `client/apps/diva/features/file/file-core/src/androidMain/kotlin/com/diva/app/file/data/services/FileSystemServiceImpl.android.kt` |
| **new** | +124 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/viewmodel/FilesViewModel.kt` |
| **new** | +102 / -0 | `client/apps/diva/features/file/file-core/src/iosMain/kotlin/com/diva/app/file/data/services/FileSystemServiceImpl.ios.kt` |
| **new** | +81 / -0 | `client/apps/diva/features/file/file-core/src/jvmMain/kotlin/com/diva/app/file/data/services/FileSystemServiceImpl.jvm.kt` |
| **new** | +80 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/data/FileRepositoryImpl.kt` |
| **new** | +75 / -0 | `client/apps/diva/features/file/file-database/src/commonMain/sqldelight/com/diva/app/file/database/Entry.sq` |
| **new** | +67 / -0 | `client/apps/diva/features/file/file-core/src/androidMain/kotlin/com/diva/app/file/domain/services/permissions/StoragePermissionRequester.android.kt` |
| **new** | +52 / -0 | `client/apps/diva/features/file/file-models/src/commonMain/kotlin/com/diva/app/file/models/MimeType.kt` |
| **new** | +42 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/domain/services/FileSystemService.kt` |
| **new** | +41 / -0 | `client/apps/diva/features/file/file-models/src/commonMain/kotlin/com/diva/app/file/models/FileCategory.kt` |
| **new** | +38 / -0 | `client/apps/diva/features/file/file-models/src/commonMain/kotlin/com/diva/app/file/models/EntryMeta.kt` |
| **new** | +36 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/domain/FileRepository.kt` |
| **new** | +31 / -0 | `client/apps/diva/features/file/file-database/src/commonMain/sqldelight/com/diva/app/file/database/EntrySchema.sq` |
| **new** | +27 / -0 | `client/apps/diva/features/file/file-models/src/commonMain/kotlin/com/diva/app/file/models/FsEntry.kt` |
| **new** | +26 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/state/DirectoryState.kt` |
| **new** | +25 / -0 | `client/apps/diva/features/file/file-database/src/commonMain/sqldelight/migrations/1.sqm` |
| **new** | +25 / -0 | `client/apps/diva/features/file/file-database/src/commonMain/sqldelight/com/diva/app/file/database/File.sq` |
| **new** | +25 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/di/Module.kt` |
| **new** | +21 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/ui/components/navigation/Routes.kt` |
| **new** | +19 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/events/DirectoryEvents.kt` |
| **new** | +19 / -0 | `client/apps/diva/features/file/file-core/build.gradle.kts` |
| **new** | +18 / -0 | `client/apps/diva/features/file/file-models/src/commonMain/kotlin/com/diva/app/file/models/DeviceFile.kt` |
| **new** | +14 / -0 | `client/apps/diva/features/file/file-models/src/commonMain/kotlin/com/diva/app/file/models/Directory.kt` |
| **new** | +13 / -0 | `client/apps/diva/features/file/file-core/src/androidMain/AndroidManifest.xml` |
| **new** | +12 / -0 | `client/apps/diva/features/file/file-core/src/jvmMain/kotlin/com/diva/app/file/di/PlatformModule.jvm.kt` |
| **new** | +12 / -0 | `client/apps/diva/features/file/file-core/src/iosMain/kotlin/com/diva/app/file/di/PlatformModule.ios.kt` |
| **new** | +12 / -0 | `client/apps/diva/features/file/file-core/src/androidMain/kotlin/com/diva/app/file/di/PlatformModule.android.kt` |
| **new** | +11 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/presentation/ui/components/navigation/FileNav.kt` |
| **new** | +10 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/domain/services/permissions/StoragePermissionRequester.kt` |
| **new** | +9 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/data/services/FileSystemServiceImpl.kt` |
| ren | +2 / -7 | `client/apps/diva/features/file/file-database/build.gradle.kts` (was `folder/folder-database`) |
| **new** | +8 / -0 | `client/apps/diva/features/file/file-core/src/jvmMain/kotlin/com/diva/app/file/domain/services/permissions/StoragePermissionRequester.jvm.kt` |
| **new** | +8 / -0 | `client/apps/diva/features/file/file-core/src/iosMain/kotlin/com/diva/app/file/domain/services/permissions/StoragePermissionRequester.ios.kt` |
| **new** | +5 / -0 | `client/apps/diva/features/file/file-core/src/commonMain/kotlin/com/diva/app/file/di/PlatformModule.kt` |
| ren | +0 / -1 | `client/apps/diva/features/file/file-models/build.gradle.kts` (was `folder/folder-models`) |

### Folder - module deleted

17 files, +0 / -582

Entirely removed: core (repository, DI, events, state, viewmodel, screen, nav, routes),
database (storage classes, SQLDelight schemas, migration) and models. Replaced by the file
feature above. `FolderNav.kt` and `Routes.kt` here are the same files the first snapshot saw
created/edited - their changes cancel out against the earlier commits.

| | +/- | File |
|---|---:|---|
| ~~del~~ | +0 / -169 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/kotlin/com/diva/app/folder/database/FolderStorage.kt`~~ |
| ~~del~~ | +0 / -55 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/kotlin/com/diva/app/folder/database/MediaFolderLinkStorage.kt`~~ |
| ~~del~~ | +0 / -53 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/data/FolderRepositoryImpl.kt`~~ |
| ~~del~~ | +0 / -48 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/screen/FolderScreen.kt`~~ |
| ~~del~~ | +0 / -41 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/sqldelight/com/diva/app/folder/database/Folder.sq`~~ |
| ~~del~~ | +0 / -31 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/domain/FolderRepository.kt`~~ |
| ~~del~~ | +0 / -26 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/di/Module.kt`~~ |
| ~~del~~ | +0 / -25 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/viewmodel/FolderViewModel.kt`~~ |
| ~~del~~ | +0 / -24 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/sqldelight/migrations/1.sqm`~~ |
| ~~del~~ | +0 / -23 | ~~`client/apps/diva/features/folder/folder-models/src/commonMain/kotlin/com/diva/app/folder/models/Folder.kt`~~ |
| ~~del~~ | +0 / -22 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/sqldelight/com/diva/app/folder/database/FolderSchema.sq`~~ |
| ~~del~~ | +0 / -18 | ~~`client/apps/diva/features/folder/folder-core/build.gradle.kts`~~ |
| ~~del~~ | +0 / -15 | ~~`client/apps/diva/features/folder/folder-database/src/commonMain/sqldelight/com/diva/app/folder/database/MediaFolderLink.sq`~~ |
| ~~del~~ | +0 / -11 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/components/navigation/Routes.kt`~~ |
| ~~del~~ | +0 / -11 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/components/navigation/FolderNav.kt`~~ |
| ~~del~~ | +0 / -5 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/state/FolderState.kt`~~ |
| ~~del~~ | +0 / -5 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/events/FolderEvents.kt`~~ |

### Home

8 files, +540 / -84

Still the divergent one: `HomeNav.kt` is edited in place rather than replaced, `HomeMock.kt`
was created and deleted inside the branch, and a new `NavDirectionRow.kt` navigation component
was added.

| | +/- | File |
|---|---:|---|
| mod | +217 / -79 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/screen/HomeScreen.kt` |
| mod | +186 / -2 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/components/HomeContent.kt` |
| **new** | +70 / -0 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/components/navigation/NavDirectionRow.kt` |
| mod | +33 / -0 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/viewmodel/HomeViewModel.kt` |
| mod | +15 / -1 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/state/HomeState.kt` |
| mod | +14 / -0 | `client/apps/diva/features/home/home-core/build.gradle.kts` |
| mod | +3 / -2 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/components/navigation/HomeNav.kt` |
| mod | +2 / -0 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/events/HomeEvents.kt` |

### Library

9 files, +282 / -75

`LibraryContent.kt` from the first snapshot is gone; `LibraryScreen.kt` was rewritten in place.
The repository/domain side also changed (`LibraryRepositoryImpl` -34, interface -13).

| | +/- | File |
|---|---:|---|
| mod | +94 / -17 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/screen/LibraryScreen.kt` |
| **new** | +87 / -0 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/state/LibraryMock.kt` |
| mod | +47 / -5 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/viewmodel/LibraryViewModel.kt` |
| mod | +24 / -2 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/state/LibraryState.kt` |
| mod | +12 / -34 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/data/LibraryRepositoryImpl.kt` |
| mod | +8 / -1 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/events/LibraryEvents.kt` |
| mod | +5 / -2 | `client/apps/diva/features/library/library-core/build.gradle.kts` |
| mod | +4 / -13 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/domain/LibraryRepository.kt` |
| mod | +1 / -1 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/components/navigation/LibraryNav.kt` |

### Player

15 files, +660 / -58

Same shape as the first snapshot: keeps `Screen`/`Nav`, gains `MiniPlayer.kt`, `Duration.kt`
and `PlayerMock.kt`, plus repository changes (`PlayerRepositoryImpl` +34/-21) and the
mechanical model cleanup in `player-models`.

| | +/- | File |
|---|---:|---|
| mod | +325 / -13 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/screen/PlayerScreen.kt` |
| **new** | +91 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/components/MiniPlayer.kt` |
| **new** | +75 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/state/PlayerMock.kt` |
| mod | +54 / -2 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/viewmodel/PlayerViewModel.kt` |
| mod | +34 / -21 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/data/PlayerRepositoryImpl.kt` |
| mod | +28 / -1 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/state/PlayerState.kt` |
| mod | +20 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/events/PlayerEvents.kt` |
| mod | +11 / -12 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/domain/PlayerRepository.kt` |
| **new** | +18 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/util/Duration.kt` |
| mod | +3 / -0 | `client/apps/diva/features/player/player-core/build.gradle.kts` |
| mod | +1 / -1 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/components/navigation/PlayerNav.kt` |
| mod | +0 / -2 | `client/apps/diva/features/player/player-models/src/commonMain/kotlin/com/diva/app/player/playback/models/ResumePoint.kt` |
| mod | +0 / -2 | `client/apps/diva/features/player/player-models/src/commonMain/kotlin/com/diva/app/player/playback/models/PlaybackHistory.kt` |
| mod | +0 / -2 | `client/apps/diva/features/player/player-models/src/commonMain/kotlin/com/diva/app/player/models/RepeatMode.kt` |
| mod | +0 / -2 | `client/apps/diva/features/player/player-models/src/commonMain/kotlin/com/diva/app/player/models/PlayerSetting.kt` |

### Profile

15 files, +346 / -240

`ProfileContent.kt` (the first snapshot's +316 file) was deleted; `ProfileScreen.kt` was
rewritten in place and split into three new subcomponents. `ProfileRepositoryImpl` and
`ProfileRepository` are deleted - `ProfileViewModel` now injects the shared app-lib
`UserRepository` and collects `observeCurrent()`. `profile-models/Profile.kt` is gone (the
`profile-models` Gradle file is reused by `settings-models` via rename).

| | +/- | File |
|---|---:|---|
| mod | +169 / -107 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/screen/ProfileScreen.kt` |
| ~~del~~ | +0 / -75 | ~~`client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/data/ProfileRepositoryImpl.kt`~~ |
| **new** | +53 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/InfoSection.kt` |
| **new** | +49 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/NavigationRow.kt` |
| **new** | +18 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/SectionTitle.kt` |
| **new** | +12 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/state/ProfileMock.kt` |
| mod | +16 / -11 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/viewmodel/ProfileViewModel.kt` |
| mod | +10 / -3 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/state/ProfileState.kt` |
| mod | +9 / -1 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/events/ProfileEvents.kt` |
| mod | +7 / -5 | `client/apps/diva/features/profile/profile-core/build.gradle.kts` |
| ~~del~~ | +0 / -20 | ~~`client/apps/diva/features/profile/profile-models/src/commonMain/kotlin/com/diva/app/profile/models/Profile.kt`~~ |
| ~~del~~ | +0 / -9 | ~~`client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/domain/ProfileRepository.kt`~~ |
| mod | +1 / -7 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/di/Module.kt` |
| mod | +1 / -1 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/navigation/Routes.kt` |
| mod | +1 / -1 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/navigation/ProfileNav.kt` |

### Search

16 files, +519 / -166

`SearchScreen.kt` (-88) is replaced by `SearchResultsScreen.kt` (+183); the first snapshot's
`SearchResultsContent.kt` and search-core `SearchField.kt` were deleted (a shared
`SearchField` now lives in `app-lib` UI instead). `SearchMock.kt` and `SearchTypeTags.kt`
survive, plus new `IconResultRow.kt` / `SectionHeader.kt` components.

| | +/- | File |
|---|---:|---|
| **new** | +183 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/screen/SearchResultsScreen.kt` |
| **new** | +119 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchMock.kt` |
| ~~del~~ | +0 / -88 | ~~`client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/screen/SearchScreen.kt`~~ |
| **new** | +60 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/IconResultRow.kt` |
| **new** | +40 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchTypeTags.kt` |
| **new** | +30 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/SectionHeader.kt` |
| mod | +33 / -36 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/data/SearchRepositoryImpl.kt` |
| mod | +19 / -17 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/viewmodel/SearchViewModel.kt` |
| mod | +9 / -1 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/events/SearchEvents.kt` |
| mod | +8 / -14 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/navigation/Routes.kt` |
| mod | +8 / -1 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchState.kt` |
| mod | +4 / -4 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/navigation/SearchNav.kt` |
| mod | +2 / -2 | `client/apps/diva/features/search/search-models/src/commonMain/kotlin/com/diva/app/search/models/SearchResults.kt` |
| mod | +2 / -1 | `client/apps/diva/features/search/search-core/build.gradle.kts` |
| mod | +1 / -1 | `client/apps/diva/features/search/search-models/build.gradle.kts` |
| mod | +1 / -1 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/domain/SearchRepository.kt` |

### Settings - relocated from app-lib, first real implementation

17 files, +308 / -8

Three presentation files are **renames** out of `client/packages/shared/app-lib` into
`client/apps/diva` (marked `ren` below); everything else is new. Note the brand-new
`settings-database` module with its own SQLDelight schema and migration.

| | +/- | File |
|---|---:|---|
| **new** | +55 / -0 | `client/apps/diva/features/settings/settings-database/src/commonMain/kotlin/com/diva/app/settings/database/SettingsStorageImpl.kt` |
| **new** | +41 / -0 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/presentation/viewmodel/SettingsViewModel.kt` |
| **new** | +40 / -0 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/data/SettingsRepositoryImpl.kt` |
| **new** | +26 / -0 | `client/apps/diva/features/settings/settings-database/build.gradle.kts` |
| **new** | +24 / -0 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/di/Module.kt` |
| ren | +23 / -4 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/presentation/ui/screen/SettingsScreen.kt` (was in app-lib) |
| **new** | +20 / -0 | `client/apps/diva/features/settings/settings-core/build.gradle.kts` |
| **new** | +15 / -0 | `client/apps/diva/features/settings/settings-database/src/commonMain/kotlin/com/diva/app/settings/database/SettingsStorage.kt` |
| **new** | +14 / -0 | `client/apps/diva/features/settings/settings-models/src/commonMain/kotlin/com/diva/app/settings/models/AppSettings.kt` |
| **new** | +12 / -0 | `client/apps/diva/features/settings/settings-database/src/commonMain/sqldelight/com/diva/app/settings/database/Settings.sq` |
| **new** | +10 / -0 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/presentation/state/SettingsState.kt` |
| **new** | +9 / -0 | `client/apps/diva/features/settings/settings-database/src/commonMain/sqldelight/migrations/1.sqm` |
| **new** | +9 / -0 | `client/apps/diva/features/settings/settings-database/src/commonMain/sqldelight/com/diva/app/settings/database/SettingsSchema.sq` |
| **new** | +6 / -0 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/domain/SettingsRepository.kt` |
| ren | +2 / -2 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/presentation/ui/components/navigation/SettingsNav.kt` (was in app-lib) |
| ren | +1 / -1 | `client/apps/diva/features/settings/settings-models/build.gradle.kts` (was `profile/profile-models`) |
| ren | +1 / -1 | `client/apps/diva/features/settings/settings-core/src/commonMain/kotlin/com/diva/app/settings/presentation/events/SettingsEvents.kt` (was in app-lib) |

### Small repo/model refactors

Collection, media, mix, playlist and server get no UI work - only repository interface/impl
changes and the mechanical model cleanup. 42 files, +96 / -130 combined.

| | +/- | File |
|---|---:|---|
| mod | +8 / -5 | `client/apps/diva/features/collection/collection-core/src/commonMain/kotlin/com/diva/app/collection/data/CollectionRepositoryImpl.kt` |
| mod | +7 / -7 | `client/apps/diva/features/collection/collection-models/src/commonMain/kotlin/com/diva/app/collection/models/Collection.kt` |
| mod | +3 / -4 | `client/apps/diva/features/collection/collection-core/src/commonMain/kotlin/com/diva/app/collection/domain/CollectionRepository.kt` |
| mod | +2 / -35 | 16 more `collection-models` files, all the mechanical `-2` cleanup except `Playlist.kt` (+1/-3) and `PlaylistSuggestions.kt` (-4) |
| mod | +13 / -6 | `client/apps/diva/features/media/media-core/src/commonMain/kotlin/com/diva/app/media/data/TagRepositoryImpl.kt` |
| mod | +9 / -4 | `client/apps/diva/features/media/media-core/src/commonMain/kotlin/com/diva/app/media/data/MediaRepositoryImpl.kt` |
| mod | +7 / -4 | `client/apps/diva/features/media/media-core/src/commonMain/kotlin/com/diva/app/media/data/MediaMetadataRepositoryImpl.kt` |
| mod | +9 / -28 | 3 `media` domain interfaces + 8 `media-models` files (mechanical `-2`) |
| mod | +9 / -8 | `client/apps/diva/features/mix/mix-core/src/commonMain/kotlin/com/diva/app/mix/data/MixRepositoryImpl.kt` |
| mod | +4 / -5 | `client/apps/diva/features/mix` domain + `MixMetadataStorage.kt` |
| mod | +10 / -8 | `client/apps/diva/features/playlist/playlist-core/src/commonMain/kotlin/com/diva/app/playlist/data/PlaylistRepositoryImpl.kt` |
| mod | +3 / -6 | `client/apps/diva/features/playlist` domain + build file |
| mod | +9 / -4 | `client/apps/diva/features/server/server-core/src/commonMain/kotlin/com/diva/app/server/data/ServerRepositoryImpl.kt` |
| mod | +3 / -6 | `client/apps/diva/features/server` domain + model |

## Shared lib features (`app-lib`)

94 files, +319 / -462. Two stories here:

- **The settings split** (`libfeature-settings`, 14 files, +49 / -123): the app-facing half
  moved into the app (see Settings above). What stays behind is renamed to
  `SharedSettingsRepository` (+16), `SharedSettingsViewModel` (+18), `SharedSettingsState`
  (+9); `AppSettings.kt` -> `Settings.kt`; the old database module, DI module and
  non-shared repository/state/viewmodel are deleted.

  | | +/- | File |
  |---|---:|---|
  | ~~del~~ | +0 / -50 | ~~`client/packages/shared/app-lib/features/settings/settings-database/build.gradle.kts`~~ |
  | ~~del~~ | +0 / -27 | ~~`.../settings/presentation/viewmodel/SettingsViewModel.kt`~~ |
  | **new** | +18 / -0 | `.../settings/presentation/viewmodel/SharedSettingsViewModel.kt` |
  | ~~del~~ | +0 / -18 | ~~`.../settings/di/Module.kt`~~ |
  | **new** | +16 / -0 | `.../settings/domain/SharedSettingsRepository.kt` |
  | **new** | +9 / -0 | `.../settings/presentation/state/SharedSettingsState.kt` |
  | ren | +3 / -2 | `.../settings/models/Settings.kt` (was `AppSettings.kt`) |
  | mod | +3 / -1 | `client/packages/shared/app-lib/features/settings/settings-core/build.gradle.kts` |
  | ~~del~~ | +0 / -25 | ~~6 more files: old `SettingsState`, `SettingsRepository`, `SettingsRepositoryImpl`, sqldelight schemas + migration~~ |

- **The repository sweep** (user 47 files +176/-240, session 7 +63/-36, auth 10 +3/-22,
  permissions 8 +15/-21, devices 4 +13/-13, verification 4 +0/-7). Domain interfaces and
  their impls were reworked in pairs; 26 of the 47 user-model files (plus 8 auth, 4 session,
  4 permissions, 3 verification, 2 devices) are the identical mechanical `-2` cleanup and are
  omitted below. The substantive files:

  | | +/- | File |
  |---|---:|---|
  | mod | +65 / -11 | `.../user/data/UserRepositoryImpl.kt` |
  | mod | +40 / -14 | `.../session/domain/Repository.kt` |
  | mod | +15 / -17 | `.../user/data/UserPreferencesRepositoryImpl.kt` |
  | mod | +14 / -20 | `.../user/data/UserPermissionsRepositoryImpl.kt` |
  | mod | +14 / -13 | `.../user/data/UserStateRepositoryImpl.kt` |
  | mod | +14 / -13 | `.../user/data/UserProfileRepositoryImpl.kt` |
  | mod | +11 / -17 | `.../user/data/UserActionsRepositoryImpl.kt` |
  | mod | +11 / -16 | `.../user/data/UserDevicesRepositoryImpl.kt` |
  | mod | +18 / -10 | `.../session/data/SessionRepositoryImpl.kt` |
  | mod | +10 / -5 | `.../permissions/data/PermissionsRepositoryImpl.kt` |
  | mod | +10 / -5 | `.../devices/data/DevicesRepositoryImpl.kt` |
  | mod | +5 / -4 | `.../session/domain/SessionRepository.kt` |
  | mod | +3 / -3 | `.../auth/data/api/client/AuthApi.kt` |
  | mod | +0 / -3 | `.../auth/presentation/ui/components/navigation/AuthNav.kt` |
  | mod | +24 / -65 | 7 `user` domain interfaces (`UserRepository`, `UserActionsRepository`, `UserDevicesRepository`, `UserPermissionsRepository`, `UserPreferencesRepository`, `UserProfileRepository`, `UserStateRepository`) - all shrink the same way |
  | mod | +8 / -16 | `user` storage/api files + a few slightly-edited models (`UserPermission`, `UserState`, `User`, `UserDevice`) |

## Shared UI / design system

31 files, +1677 / -341

The navigation rewrite plus a batch of new shared components. Highlights: the rewritten
`TabNavigator.kt`, the **unreferenced** `TabNavigatorAlternative.kt`, the deleted
`TabNavHost.kt`, `NavSurfaceState.kt`, new layout buttons, a new test file, the multiplatform
`Carousel` (common + 5 platform `actual` files), and input components (`SearchField`,
`SecureTextField`).

| | +/- | File |
|---|---:|---|
| mod | +164 / -153 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/TabNavigator.kt` |
| **new** | +268 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/TabNavigatorAlternative.kt` |
| **new** | +232 / -0 | `client/packages/shared/framework/diva-ui-test/src/commonTest/kotlin/io/github/juevigrace/diva/ui/test/TabNavigatorTest.kt` |
| **new** | +170 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/Carousel.kt` |
| mod | +91 / -57 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/AdaptiveScreen.kt` |
| **new** | +119 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/input/SearchField.kt` |
| **new** | +100 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/input/SecureTextField.kt` |
| **new** | +90 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/NavSurfaceState.kt` |
| **new** | +79 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/buttons/CompactIconButton.kt` |
| mod | +54 / -20 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/Navigator.kt` |
| **new** | +54 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/buttons/NotificationsIconButton.kt` |
| mod | +29 / -28 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/RailScreen.kt` |
| **new** | +44 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/buttons/ProfileIconButton.kt` |
| mod | +22 / -25 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/ModalDrawerScreen.kt` |
| mod | +19 / -22 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/PermanentDrawerScreen.kt` |
| **new** | +36 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/buttons/DrawerIconButton.kt` |
| mod | +19 / -5 | `client/packages/shared/framework/diva-ui-test/src/commonTest/kotlin/io/github/juevigrace/diva/ui/test/NavigatorTest.kt` |
| **new** | +20 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/permissions/RequestPermission.kt` |
| **new** | +16 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.kt` |
| **new** | +9 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/input/SearchFieldEvents.kt` |
| mod | +8 / -1 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/navigation/NavStyle.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/androidMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.android.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/iosMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.ios.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/jsMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.js.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/jvmMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.jvm.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/wasmJsMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.wasmJs.kt` |
| mod | +2 / -0 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/Tab.kt` |
| mod | +1 / -4 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/Screen.kt` |
| mod | +1 / -0 | `client/packages/shared/app-lib/ui/build.gradle.kts` |
| ~~del~~ | +0 / -25 | ~~`client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/TabNavHost.kt`~~ |
| mod | +0 / -1 | `client/packages/shared/framework/diva-ui/src/wasmJsMain/kotlin/io/github/juevigrace/diva/ui/navigation/BackHandler.wasmJs.kt` |

## Core / permission framework

16 files, +314 / -18

New multiplatform permission-requester framework in `diva-core` (expect in commonMain; real
Android implementation with an App-Startup-registered `ActivityProvider`; iOS/JVM stubs),
plus a `diva-ui` `RequestPermission` composable (counted above). The `app-lib/core` changes
are just the mechanical cleanup.

| | +/- | File |
|---|---:|---|
| **new** | +80 / -0 | `client/packages/shared/framework/diva-core/src/commonMain/kotlin/io/github/juevigrace/diva/core/permissions/PermissionRequester.kt` |
| **new** | +80 / -0 | `client/packages/shared/framework/diva-core/src/androidMain/kotlin/io/github/juevigrace/diva/core/permissions/activity/ActivityProvider.kt` |
| **new** | +78 / -0 | `client/packages/shared/framework/diva-core/src/androidMain/kotlin/io/github/juevigrace/diva/core/permissions/AndroidPermissionRequester.kt` |
| **new** | +23 / -0 | `client/packages/shared/framework/diva-core/src/androidMain/kotlin/io/github/juevigrace/diva/core/permissions/activity/ActivityLifecycleObserver.kt` |
| **new** | +17 / -0 | `client/packages/shared/framework/diva-core/src/androidMain/AndroidManifest.xml` |
| **new** | +11 / -0 | `client/packages/shared/framework/diva-core/src/androidMain/kotlin/io/github/juevigrace/diva/core/permissions/activity/ActivityProviderInitializer.kt` |
| **new** | +8 / -0 | `client/packages/shared/framework/diva-core/src/jvmMain/kotlin/io/github/juevigrace/diva/core/permissions/JvmPermissionRequester.kt` |
| **new** | +8 / -0 | `client/packages/shared/framework/diva-core/src/iosMain/kotlin/io/github/juevigrace/diva/core/permissions/IosPermissionRequester.kt` |
| mod | +8 / -2 | `client/packages/shared/framework/diva-core/src/commonMain/kotlin/io/github/juevigrace/diva/core/Option.kt` |
| mod | +1 / -3 | `client/packages/shared/framework/diva-core/src/commonMain/kotlin/io/github/juevigrace/diva/core/DivaJsExport.kt` |
| mod | +0 / -3 | `client/packages/shared/framework/diva-core/src/jsMain/kotlin/io/github/juevigrace/diva/core/DivaJsExport.kt` |
| mod | +0 / -10 | 5 `app-lib/core` model/validation files (mechanical cleanup) |

## Database

14 files, +143 / -39

`DivaDatabase.kt` reworked (+43/-15), the database test kit updated to match (including an
android host-test `DriverProvider` rewrite), and every platform `DriverProvider` gets the same
+2/-1 change (likely a constructor/signature change - spot-check one). The sqlite
`SqliteExceptionTransformer` test fix is in `61a9e8e0`. Feature-level database modules are
counted under their features (file, settings).

| | +/- | File |
|---|---:|---|
| mod | +60 / -8 | `client/packages/shared/framework/diva-database-test/diva-database-test-kmp/src/androidHostTest/kotlin/io/github/juevigrace/diva/database/test/DriverProvider.kt` |
| mod | +43 / -15 | `client/packages/shared/framework/diva-database/src/commonMain/kotlin/io/github/juevigrace/diva/database/DivaDatabase.kt` |
| mod | +17 / -3 | `client/packages/shared/framework/diva-database-test/diva-database-test-kmp/src/commonTest/kotlin/io/github/juevigrace/diva/database/test/DivaDatabaseTest.kt` |
| mod | +10 / -5 | 5 `diva-database-sqlite` platform `DriverProvider` files (+2/-1 each) |
| mod | +4 / -2 | `diva-database-mysql` + `diva-database-postgres` driver providers |
| mod | +5 / -2 | `client/apps/diva/database/build.gradle.kts` |
| mod | +3 / -1 | `client/apps/diva/database/src/commonMain/kotlin/com/diva/app/database/DBMappers.kt` |
| mod | +1 / -1 | `client/packages/shared/framework/diva-database-sqlite/src/androidMain/kotlin/io/github/juevigrace/diva/database/sqlite/exception/SqliteExceptionTransformer.kt` |
| mod | +0 / -2 | `client/packages/shared/app-lib/database/build.gradle.kts` |

## App wiring and resources

19 files, +719 / -31 (plus 1 file counted under "other": `client/apps/diva/core/.../VisibilityType.kt`, -2)

New app-level UI kit (`Artwork`, `ArtworkCard`, `CarouselCard`, `CarouselSection`,
`TypeBadge`), a new `AppViewModel` + `AppState`, DI updates (`AppModule` +26/-4,
`NavigationModule`, `NetworkModule` -4), platform entry-point updates (Android, desktop, iOS)
and new string resources (incl. a new `values-es` file).

| | +/- | File |
|---|---:|---|
| **new** | +134 / -0 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/presentation/viewmodel/AppViewModel.kt` |
| **new** | +117 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/CarouselCard.kt` |
| **new** | +97 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/Artwork.kt` |
| **new** | +82 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/CarouselSection.kt` |
| **new** | +79 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/ArtworkCard.kt` |
| mod | +55 / -13 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/presentation/ui/screen/App.kt` |
| **new** | +53 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/TypeBadge.kt` |
| mod | +26 / -4 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/di/AppModule.kt` |
| mod | +16 / -3 | `client/apps/diva/shared-ui/src/iosMain/kotlin/io/github/juevigrace/diva/lib/shared/Main.kt` |
| mod | +14 / -1 | `client/apps/diva/platforms/desktopApp/src/main/kotlin/com/diva/app/main.kt` |
| mod | +11 / -1 | `client/apps/diva/platforms/androidApp/src/main/kotlin/com/diva/app/DivaApp.kt` |
| mod | +9 / -2 | `client/apps/diva/shared-ui/build.gradle.kts` |
| **new** | +11 / -0 | `client/apps/diva/resources/src/commonMain/composeResources/values-es/strings.xml` |
| **new** | +8 / -0 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/presentation/state/AppState.kt` |
| mod | +4 / -0 | `client/apps/diva/resources/src/commonMain/composeResources/values/strings.xml` |
| mod | +3 / -3 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/di/NavigationModule.kt` |
| **new** | binary | `client/apps/diva/resources/src/commonMain/composeResources/drawable/ic_folder.webp` |
| **new** | binary | `client/apps/diva/resources/src/commonMain/composeResources/drawable/puerro.webp` |
| mod | +0 / -4 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/di/NetworkModule.kt` |

`app-lib` also gains its own `resources` module (5 files, +25): build file, `strings.xml` +
`values-es`, and `ic_eye.webp` / `ic_eye_off.webp` drawables.

## Network layer

4 files, +9 / -7

| | +/- | File |
|---|---:|---|
| mod | +3 / -4 | `client/packages/shared/framework/diva-network/src/commonMain/kotlin/io/github/juevigrace/diva/network/client/Plugins.kt` |
| mod | +2 / -1 | `client/packages/shared/framework/diva-network/src/commonMain/kotlin/io/github/juevigrace/diva/network/client/WebSocket.kt` |
| mod | +2 / -1 | `client/packages/shared/framework/diva-network/src/commonMain/kotlin/io/github/juevigrace/diva/network/client/SSE.kt` |
| mod | +2 / -1 | `client/packages/shared/framework/diva-network/src/commonMain/kotlin/io/github/juevigrace/diva/network/client/DivaClient.kt` |

## Tooling / build changes

30 files, +169 / -119

Still mostly the **Gradle 9.7.1 -> 9.8.0 wrapper bump** across all 5 included builds, but two
new things since the first snapshot:

- **`gradle-daemon-jvm.properties` rewritten in all 5 builds** (+12/-11 each) - toolchain
  vendor/selection churn, most likely IDE-generated. Worth one confirmation that it is
  intentional, then skip.
- **New/changed convention plugins** in `build-logic`: `divabuild.library-app-ui.gradle.kts`
  (+4), `divabuild.targets-mobile.gradle.kts` (+2), `divabuild.targets-web.gradle.kts` (+1),
  `divabuild.compose.gradle.kts` (+1); `divabuild.app-android` and `divabuild.app-desktop`
  each lose one line. Proguard/consumer rules gain 7-8 lines each.

Substantive files:

| | +/- | File |
|---|---:|---|
| mod | +9 / -2 | `client/gradle/libs.versions.toml` |
| mod | +8 / -0 | `client/build-logic/src/main/resources/proguard-rules.pro` |
| mod | +7 / -4 | `client/apps/diva/settings.gradle.kts` (file/settings modules in, folder out) |
| mod | +7 / -0 | `client/build-logic/src/main/resources/consumer-rules.pro` |
| mod | +4 / -0 | `client/build-logic/src/main/kotlin/divabuild.library-app-ui.gradle.kts` |
| mod | +3 / -9 | `client/build.gradle.kts` |
| mod | +12 / -11 | 5 `gradle/gradle-daemon-jvm.properties` files (one per build) |
| mod | +2 / -0 | `client/build-logic/src/main/kotlin/divabuild.targets-mobile.gradle.kts` |
| mod | +1 / -3 | 5 `gradle/wrapper/gradle-wrapper.properties` files (the version bump) |
| mod | +1 / -0 | `client/build-logic/src/main/kotlin/divabuild.targets-web.gradle.kts` |
| mod | +1 / -0 | `client/build-logic/src/main/kotlin/divabuild.compose.gradle.kts` |
| mod | +4 / -4 | 3 `gradle.properties` files (apps/diva, app-lib, framework) |
| mod | +2 / -2 | `client/gradlew`, `client/gradle.properties`, `client/build-logic/gradle.properties` |
| mod | +0 / -1 | `divabuild.app-android.gradle.kts`, `divabuild.app-desktop.gradle.kts` |
| mod | +1 / -1 | `client/packages/shared/app-lib/settings.gradle.kts` |
| mod | +43 / -13 | `client/gradlew.bat` |
| mod | binary | `client/gradle/wrapper/gradle-wrapper.jar` |

## Review flags

Things a reviewer should consciously decide on, roughly by importance:

1. **`TabNavigatorAlternative.kt` (268 lines) is referenced nowhere in the codebase.** Dead
   experimental code - either delete it or wire it up before merge.
2. **The first snapshot's `*Content.kt` refactor was fully reverted** in `4d9ce3e4`, and an
   entire `feed` feature was added and removed (`adbcb16e`). 37 files were created and deleted
   inside the branch (13 file, 9 feed, 4 home, 3 folder, 2 search, 1 profile, 1 library, plus 3
   markdown notes) - **none of that churn is visible in the cumulative diff**. Consider
   squashing/cleaning the history before merge.
3. **This review file is itself committed on the branch** (`14894d5c`) - decide whether it
   belongs in the merge.
4. **The `folder` -> `file` module swap deletes user-facing folder browsing** (schema,
   storage and UI) in favor of device file-system browsing - confirm that is the intended
   product direction, not a prototype.
5. **Profile's data layer is gone**: `ProfileRepositoryImpl`/`ProfileRepository` deleted,
   `ProfileViewModel` now depends on the shared `UserRepository`. Check the Koin wiring in
   `profileModule()` and that profile features (avatar, etc.) are covered by user models.
6. **Home remains mid-refactor**: `HomeNav.kt` edited in place, `NavDirectionRow.kt` added,
   four home files created-and-deleted inside the branch.
7. **80 files carry the identical mechanical `ExperimentalJsExport` opt-in removal**
   (`cf053880`) - skim none, just confirm CI is green.
8. **`gradle-daemon-jvm.properties` rewritten in all 5 builds** - confirm intentional.

## Suggested review order

1. `TabNavigator.kt` + the deleted `TabNavHost.kt` + `NavSurfaceState.kt` + `Navigator.kt` +
   `TabNavigatorTest.kt`, then decide the fate of `TabNavigatorAlternative.kt` - the core
   navigation model everything else hangs off.
2. The `file` feature end-to-end (`FilesScreen`, `FilesViewModel`, `FileSystemService`
   actuals, `EntryStorage` + `Entry.sq`, `StoragePermissionRequester`) together with the
   `diva-core` permission framework and the folder deletion in `settings.gradle.kts` - the
   biggest and most behavior-heavy change on the branch.
3. Settings: new `features/settings` module (+ its `settings-database`) against the deleted
   app-lib half and the `SharedSettings*` remnant - a module-boundary change, review as one
   unit.
4. The rewritten screens: `PlayerScreen.kt` (largest single-file change), `HomeScreen.kt` +
   `HomeContent.kt`, `ProfileScreen.kt` (+ its three new subcomponents), `SearchResultsScreen.kt`,
   `LibraryScreen.kt` - pure UI/design work, skim for consistency.
5. Shared UI components: `Carousel` + `Artwork`/`ArtworkCard`/`CarouselCard`/`CarouselSection`/
   `TypeBadge`, `SearchField`/`SecureTextField`, the new icon buttons - skim.
6. `AppViewModel.kt`, `App.kt`, `AppModule.kt` and the three platform entry points - how the
   new pieces get wired up.
7. The repository sweep (user/session/devices/permissions/auth + the small app features) -
   mostly mechanical; spot-check `UserRepositoryImpl.kt` and `session/domain/Repository.kt`.
8. `DivaDatabase.kt` + one platform `DriverProvider` + the sqlite exception-transformer fix.
9. `libs.versions.toml`, then one `gradle-daemon-jvm.properties`, then stop.
