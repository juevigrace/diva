# Diva Project TODO

## Pending Refactors

### 1. Option Refactor & JS Export Fix
- [x] Update all project imports:
    - Replace `Option.Some` with `Some`.
    - Replace `Option.None` with `None`.
- [x] Add missing imports for `io.github.juevigrace.diva.core.Some` and `io.github.juevigrace.diva.core.None` across all modules.
- [ ] Verify that `Option` hierarchy is correctly exported for JS consumption. (Build failing due to env issues)
- [x] Fix any generic constraint issues (`T : Any`) introduced during the refactor.
- [x] Extract `Option` extensions to `OptionExtensions.kt` and suppress detekt warnings.

### 2. Database Exception Mapping
- [x] Define `DivaDatabaseException` hierarchy.
- [x] Implement `DatabaseExceptionTransformer` interface.
- [x] Implement `SqliteExceptionTransformer` for all platforms.
- [x] Implement `MysqlExceptionTransformer` and `PostgresExceptionTransformer`.
- [x] Wire transformers into `DivaDatabaseImpl`.
- [ ] Verify error propagation in repositories with unit tests.

## Future Tasks
- [ ] Implement business logic in feature repositories.
- [ ] Implement ViewModels and UI logic for new feature modules (Permissions, Devices).
- [ ] Complete network integration using `DivaClient`.
- [ ] Resolve global build buildAll task environment issues.
