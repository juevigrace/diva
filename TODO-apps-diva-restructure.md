# TODO — apps/diva restructure (W1 modules, W2 packages, W3 SQLDelight)

State: clean tree @ `9ed69f4a`. app-lib models move already done.
Order matters: **W1 → W2 → W3**. W1 is a prerequisite for W2 (namespace vs package).

---

## W1 — module restructure

### W1.1 build-logic: preserve Android namespace  ← do first, blocks W2
- `client/build-logic/src/main/kotlin/divabuild/internal/Module.kt`
  add `internal fun Project.appModuleNamespace(prefix: String) = "$prefix.${moduleSlug().dottedModuleId()}"`
- `client/build-logic/src/main/kotlin/divabuild.diva-app.gradle.kts`
  switch `moduleNamespace("com.diva.app")` -> `appModuleNamespace("com.diva.app")`
- DO NOT touch `moduleNamespace` — `divabuild.framework-base` depends on it
- Why: `diva-app` derives namespace from `project.name`; `:features:home` -> `:features:home:home-core`
  would silently yield `com.diva.app.home.core`
- Result must be byte-identical to today for all 26 `diva-app` modules

### W1.2 move 19 module directories (git mv)
- `features/<f>/`         -> `features/<f>/<f>-core/`         (11: collection folder home library media mix player playlist profile search server)
- `features/<f>/database/` -> `features/<f>/<f>-database/`     (8: collection folder library media mix player playlist server)

### W1.3 apps/diva/settings.gradle.kts
- 11 flat includes `:features:<f>` -> `:features:<f>:<f>-core`
- 8 `include(...)` + `projectDir` pairs -> plain `include(":features:<f>:<f>-database")`
  removes all 8 `projectDir` lines
- unchanged: app-lib substitution map, `includeBuild`s, 7 flat modules, `rootProject.name = "diva-app"`

### W1.4 type-safe accessors — 45 refs / 18 files
- `projects.features.mediaDatabase` -> `projects.features.media.mediaDatabase`
- `projects.features.library`       -> `projects.features.library.libraryCore`

### W1.5 string project() refs — 16 refs / 7 files
- 8 in `database/build.gradle.kts` (SQLDelight `linkTo`)
- 8 in feature db modules (library/playlist/mix/folder/collection/player `database`)
- `project(":features:media-database")` -> `project(":features:media:media-database")`

### W1.6 cleanup
- remove orphaned `features/<f>/build` and `features/<f>/database/build` (gitignored, regenerable)

### W1 checkpoint
- namespace diff vs baseline = EMPTY (19 feature values captured:
  `com.diva.app.<f>` and `com.diva.app.<f>.database`)
- declared-dependency diff = path renames only
- `apps/diva assemble` + `lint` green

---

## W2 — packages to app convention

`com.diva.app.features.<f>.*` -> `com.diva.app.<f>.*`

- relocate 121 files: `com/diva/app/features/<f>/` -> `com/diva/app/<f>/`
- rewrite 124 files (121 + 3 in `shared-ui`):
  - `shared-ui/.../di/AppModule.kt`
  - `shared-ui/.../di/NavigationModule.kt`
  - `shared-ui/.../presentation/ui/screen/App.kt`
- 11 literal prefix rewrites with `\b` guards; no feature name is a prefix of another
- untouched: `com.diva.app.models.*`, `.core`, `.database`, `.resources`, `.shared.ui`,
  `.ui`, `.presentation`, `.di`, `.generated.resources`
- no reflection/Koin package scanning — all wiring is import-based (verified)
- no collisions: 11 feature names vs existing `com.diva.app.*` packages
- no npm/JS risk: only flat `api-models`, `core`, `core-models` have JS/Wasm; their paths unchanged

### W2 checkpoint
- `apps/diva assemble` + `lint` green
- zero `com.diva.app.features` references remain

---

## W3 — SQLDelight packageName alignment (8 modules)

All 9 db modules register a database named `DivaDB` with a distinct `packageName`.
Today: `com.diva.app.database.<f>` — mismatched with the module's own Kotlin sources.

Change `packageName.set(...)` in 8 feature db modules:
- `com.diva.app.database.collection` -> `com.diva.app.collection.database`
- `com.diva.app.database.folder`      -> `com.diva.app.folder.database`
- `com.diva.app.database.library`     -> `com.diva.app.library.database`
- `com.diva.app.database.media`       -> `com.diva.app.media.database`
- `com.diva.app.database.mix`         -> `com.diva.app.mix.database`
- `com.diva.app.database.player`      -> `com.diva.app.player.database`
- `com.diva.app.database.playlist`    -> `com.diva.app.playlist.database`
- `com.diva.app.database.server`      -> `com.diva.app.server.database`
- NOT changed: root `database/build.gradle.kts` stays `com.diva.app.database`

Effect: generated queries land in the same package as the module's storage classes,
so those imports become redundant (drop them; ktlint flags same-package imports).
Callers importing `com.diva.app.database.<f>.DivaDB` need updating (9 refs found).

### W3 checkpoint
- SQLDelight regeneration + migration verification green
- `apps/diva assemble` + `lint` green

---

## Final verification (all three)

- `apps/diva assemble` + `lint` (transitively builds `framework` + `app-lib` via `includeBuild`)
- SQLDelight `linkTo` resolution across the 8 db modules
- namespace diff still empty
- full framework + app-lib builds still green

## Notes / non-goals
- `rootProject.name = "diva-app"` stays
- not touching: `com.diva.app.models.*` (app's own models in `core-models`)
- not touching: `com.diva.app.presentation`, `.di` in `shared-ui`
- build-logic `moduleNamespace` used by `framework-base` stays name-based
