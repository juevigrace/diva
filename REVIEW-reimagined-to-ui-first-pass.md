# Branch review: `reimagined` -> `reimagined-feat-ui-first-pass`

File-level overview of everything this branch changes relative to `reimagined`.

| | |
|---|---|
| Base branch | `reimagined` @ `276c182a` |
| Head branch | `reimagined-feat-ui-first-pass` @ `20ab4b81` |
| Merge base | `276c182a` |
| Relationship | base is a direct ancestor of head (linear, no rebase/merge divergence) |
| Commits | 5 |
| Files changed | 89 (24 new, 9 deleted, 56 modified) |
| Net lines | +3564 / -602 (plus 2 binary files) |

```
diff  git diff reimagined...reimagined-feat-ui-first-pass
```

## Commits

- `41ab30ff` add first ui designs
- `8773ba59` fix memory settings and date format error
- `3938da95` fix carousel buttons and scroll
- `2ac450f9` fixes
- `20ab4b81` start refactor of tabs

## Biggest changes (review these first)

Ranked by total lines touched.

| | Churn | Status | File |
|---|---:|---|---|
| **new** | 370 | new | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/components/FoldersContent.kt` |
| mod | 328 | mod | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/screen/PlayerScreen.kt` |
| **new** | 316 | new | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/ProfileContent.kt` |
| **new** | 234 | new | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/components/LibraryContent.kt` |
| **new** | 232 | new | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/SearchResultsContent.kt` |
| mod | 214 | mod | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/screen/HomeScreen.kt` |
| mod | 179 | mod | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/components/HomeContent.kt` |
| **new** | 170 | new | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/Carousel.kt` |
| ~~del~~ | 145 | del | ~~`client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/screen/ProfileScreen.kt`~~ |
| **new** | 141 | new | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/state/FolderMock.kt` |
| **new** | 118 | new | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchMock.kt` |
| **new** | 101 | new | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/SearchField.kt` |
| ~~del~~ | 88 | del | ~~`client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/screen/SearchScreen.kt`~~ |
| mod | 87 | mod | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/viewmodel/FolderViewModel.kt` |
| **new** | 87 | new | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/state/LibraryMock.kt` |

## The recurring pattern

This is mostly **one refactor applied across every feature module**, not 52 unrelated changes.
Per feature, a dedicated `Nav` composable + `Screen` composable pair is deleted and replaced by a
single self-contained `*Content.kt`, and a `*Mock.kt` state file is added to drive previews:

```
- presentation/ui/screen/FooScreen.kt              (deleted)
- presentation/ui/components/navigation/FooNav.kt  (deleted)
+ presentation/ui/components/FooContent.kt         (new)
+ presentation/state/FooMock.kt                    (new)
```

The pattern holds for **Folder, Library, Profile and Search**.

Two features diverge and deserve closer attention:

- **Home** - `HomeNav.kt` is not touched at all, and `HomeScreen.kt` / `HomeContent.kt` are both
  edited in place rather than replaced. Looks like this refactor is not finished for Home.
- **Player** - keeps its `Screen`/`Nav` and instead gains `MiniPlayer.kt` and `Duration.kt`,
  i.e. a different change entirely (a mini-player + duration formatting helper).

Both this and the deleted `TabNavHost.kt` point at the last commit, `start refactor of tabs`.

## Feature modules

52 of 89 files. Each feature also has its `Events.kt`, `*State.kt` and `*ViewModel.kt` updated
to feed the new content composables.

### Folder

9 files, +704 / -69

| | +/- | File |
|---|---:|---|
| mod | +3 / -0 | `client/apps/diva/features/folder/folder-core/build.gradle.kts` |
| mod | +24 / -1 | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/events/FolderEvents.kt` |
| **new** | +141 / -0 | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/state/FolderMock.kt` |
| mod | +59 / -2 | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/state/FolderState.kt` |
| **new** | +370 / -0 | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/components/FoldersContent.kt` |
| ~~del~~ | +0 / -11 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/components/navigation/FolderNav.kt`~~ |
| mod | +24 / -3 | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/components/navigation/Routes.kt` |
| ~~del~~ | +0 / -48 | ~~`client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/ui/screen/FolderScreen.kt`~~ |
| mod | +83 / -4 | `client/apps/diva/features/folder/folder-core/src/commonMain/kotlin/com/diva/app/folder/presentation/viewmodel/FolderViewModel.kt` |

### Home

7 files, +449 / -32

| | +/- | File |
|---|---:|---|
| mod | +4 / -0 | `client/apps/diva/features/home/home-core/build.gradle.kts` |
| mod | +9 / -1 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/events/HomeEvents.kt` |
| **new** | +45 / -0 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/state/HomeMock.kt` |
| mod | +11 / -1 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/state/HomeState.kt` |
| mod | +177 / -2 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/components/HomeContent.kt` |
| mod | +188 / -26 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/ui/screen/HomeScreen.kt` |
| mod | +15 / -2 | `client/apps/diva/features/home/home-core/src/commonMain/kotlin/com/diva/app/home/presentation/viewmodel/HomeViewModel.kt` |

### Library

7 files, +404 / -66

| | +/- | File |
|---|---:|---|
| mod | +10 / -1 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/events/LibraryEvents.kt` |
| **new** | +87 / -0 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/state/LibraryMock.kt` |
| mod | +25 / -2 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/state/LibraryState.kt` |
| **new** | +234 / -0 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/components/LibraryContent.kt` |
| ~~del~~ | +0 / -11 | ~~`client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/components/navigation/LibraryNav.kt`~~ |
| ~~del~~ | +0 / -48 | ~~`client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/ui/screen/LibraryScreen.kt`~~ |
| mod | +48 / -4 | `client/apps/diva/features/library/library-core/src/commonMain/kotlin/com/diva/app/library/presentation/viewmodel/LibraryViewModel.kt` |

### Player

8 files, +595 / -16

| | +/- | File |
|---|---:|---|
| mod | +20 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/events/PlayerEvents.kt` |
| **new** | +75 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/state/PlayerMock.kt` |
| mod | +28 / -1 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/state/PlayerState.kt` |
| **new** | +83 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/components/MiniPlayer.kt` |
| mod | +1 / -1 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/components/navigation/PlayerNav.kt` |
| mod | +316 / -12 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/screen/PlayerScreen.kt` |
| **new** | +18 / -0 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/ui/util/Duration.kt` |
| mod | +54 / -2 | `client/apps/diva/features/player/player-core/src/commonMain/kotlin/com/diva/app/player/presentation/viewmodel/PlayerViewModel.kt` |

### Profile

10 files, +414 / -179

| | +/- | File |
|---|---:|---|
| mod | +4 / -0 | `client/apps/diva/features/profile/profile-core/build.gradle.kts` |
| mod | +5 / -2 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/data/ProfileRepositoryImpl.kt` |
| mod | +9 / -1 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/events/ProfileEvents.kt` |
| **new** | +47 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/state/ProfileMock.kt` |
| mod | +8 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/state/ProfileState.kt` |
| **new** | +316 / -0 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/ProfileContent.kt` |
| ~~del~~ | +0 / -11 | ~~`client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/components/navigation/ProfileNav.kt`~~ |
| ~~del~~ | +0 / -145 | ~~`client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/ui/screen/ProfileScreen.kt`~~ |
| mod | +21 / -18 | `client/apps/diva/features/profile/profile-core/src/commonMain/kotlin/com/diva/app/profile/presentation/viewmodel/ProfileViewModel.kt` |
| mod | +4 / -2 | `client/apps/diva/features/profile/profile-models/src/commonMain/kotlin/com/diva/app/profile/models/Profile.kt` |

### Search

11 files, +537 / -132

| | +/- | File |
|---|---:|---|
| mod | +1 / -0 | `client/apps/diva/features/search/search-core/build.gradle.kts` |
| mod | +9 / -1 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/events/SearchEvents.kt` |
| **new** | +118 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchMock.kt` |
| mod | +8 / -1 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchState.kt` |
| **new** | +40 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/state/SearchTypeTags.kt` |
| **new** | +101 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/SearchField.kt` |
| **new** | +232 / -0 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/SearchResultsContent.kt` |
| mod | +8 / -14 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/navigation/Routes.kt` |
| ~~del~~ | +0 / -11 | ~~`client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/components/navigation/SearchNav.kt`~~ |
| ~~del~~ | +0 / -88 | ~~`client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/ui/screen/SearchScreen.kt`~~ |
| mod | +20 / -17 | `client/apps/diva/features/search/search-core/src/commonMain/kotlin/com/diva/app/search/presentation/viewmodel/SearchViewModel.kt` |

## Shared UI / design system

13 files, +247 / -53

Two notable pieces here: a new multiplatform `Carousel` component (common + 5 platform
`actual` files) and the removal of `TabNavHost.kt`, which is the tab-navigations code the
feature-level `*Nav.kt` deletions feed into.

| | +/- | File |
|---|---:|---|
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/androidMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.android.kt` |
| **new** | +170 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/Carousel.kt` |
| **new** | +16 / -0 | `client/packages/shared/app-lib/ui/src/commonMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/iosMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.ios.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/jsMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.js.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/jvmMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.jvm.kt` |
| **new** | +6 / -0 | `client/packages/shared/app-lib/ui/src/wasmJsMain/kotlin/io/github/juevigrace/diva/lib/ui/components/carousel/CarouselDefaults.wasmJs.kt` |
| mod | +2 / -2 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/AdaptiveScreen.kt` |
| mod | +1 / -4 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/ModalDrawerScreen.kt` |
| mod | +1 / -4 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/PermanentDrawerScreen.kt` |
| mod | +7 / -3 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/RailScreen.kt` |
| mod | +20 / -15 | `client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/layout/Screen.kt` |
| ~~del~~ | +0 / -25 | ~~`client/packages/shared/framework/diva-ui/src/commonMain/kotlin/io/github/juevigrace/diva/ui/navigation/TabNavHost.kt`~~ |

## App wiring and resources

6 files, +124 / -3

| | +/- | File |
|---|---:|---|
| **new** | binary | `client/apps/diva/resources/src/commonMain/composeResources/drawable/ic_folder.webp` |
| mod | +1 / -0 | `client/apps/diva/resources/src/commonMain/composeResources/values/strings.xml` |
| mod | +2 / -2 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/di/NavigationModule.kt` |
| mod | +6 / -1 | `client/apps/diva/shared-ui/src/commonMain/kotlin/com/diva/app/presentation/ui/screen/App.kt` |
| **new** | +62 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/Artwork.kt` |
| **new** | +53 / -0 | `client/apps/diva/ui/src/commonMain/kotlin/com/diva/app/ui/components/TypeBadge.kt` |

## Network layer

1 files, +3 / -4

| | +/- | File |
|---|---:|---|
| mod | +3 / -4 | `client/packages/shared/framework/diva-network/src/commonMain/kotlin/io/github/juevigrace/diva/network/client/Plugins.kt` |

## Tooling / build changes

Isolated on purpose - none of this is UI work. Most of it is a **Gradle 9.7.1 -> 9.8.0 wrapper**
bump propagated to all 5 included builds. The one substantive change is `libs.versions.toml`.

17 files, +87 / -48

### Substantive

| | +/- | File |
|---|---:|---|
| mod | +1 / -0 | `client/build-logic/src/main/kotlin/divabuild.compose.gradle.kts` |
| mod | +7 / -0 | `client/build-logic/src/main/resources/consumer-rules.pro` |
| mod | +8 / -0 | `client/build-logic/src/main/resources/proguard-rules.pro` |
| mod | +5 / -2 | `client/gradle/libs.versions.toml` |

### Gradle 9.7.1 -> 9.8.0 wrapper bump (mechanical, 5 builds)

| | +/- | File |
|---|---:|---|
| mod | +4 / -4 | `client/apps/diva/gradle.properties` |
| mod | +1 / -3 | `client/apps/diva/gradle/wrapper/gradle-wrapper.properties` |
| mod | +2 / -2 | `client/build-logic/gradle.properties` |
| mod | +1 / -3 | `client/build-logic/gradle/wrapper/gradle-wrapper.properties` |
| mod | +2 / -2 | `client/gradle.properties` |
| mod | binary | `client/gradle/wrapper/gradle-wrapper.jar` |
| mod | +1 / -3 | `client/gradle/wrapper/gradle-wrapper.properties` |
| mod | +2 / -2 | `client/gradlew` |
| mod | +43 / -13 | `client/gradlew.bat` |
| mod | +4 / -4 | `client/packages/shared/app-lib/gradle.properties` |
| mod | +1 / -3 | `client/packages/shared/app-lib/gradle/wrapper/gradle-wrapper.properties` |
| mod | +4 / -4 | `client/packages/shared/framework/gradle.properties` |
| mod | +1 / -3 | `client/packages/shared/framework/gradle/wrapper/gradle-wrapper.properties` |

## Suggested review order

1. `Screen.kt` + the deleted `TabNavHost.kt` and the `diva-ui` layout files - this is the core
   navigation model change everything else follows from.
2. `NavigationModule.kt` and `App.kt` - how the new content composables get wired up.
3. The new `Carousel` and `Artwork` / `TypeBadge` components, then one feature end-to-end
   (e.g. `library`, which is a clean 7-file before/after).
4. The other five features, mainly skimming for divergence from that pattern.
5. `libs.versions.toml` - the only tooling file worth a real look.
6. The Gradle wrapper bump - spot-check one file, then skip.
