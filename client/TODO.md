# Diva Project TODO

## Pending Refactors

### 1. Option Refactor & JS Export Fix
- [ ] Update all project imports:
    - Replace `Option.Some` with `Some`.
    - Replace `Option.None` with `None`.
- [ ] Add missing imports for `io.github.juevigrace.diva.core.Some` and `io.github.juevigrace.diva.core.None` across all modules:
    - `:packages:shared:framework:diva-core`
    - `:packages:shared:framework:diva-ui`
    - `:packages:shared:app-lib:diva-lib-models-core`
    - `:packages:shared:app-lib:diva-lib-models-api`
    - `:app-lib:features:*`
- [ ] Verify that `Option` hierarchy is correctly exported for JS consumption.
- [ ] Fix any generic constraint issues (`T : Any`) introduced during the refactor.

### 2. Database Exception Mapping
- [x] Define `DivaDatabaseException` hierarchy.
- [x] Implement `DatabaseExceptionTransformer` interface.
- [x] Implement `SqliteExceptionTransformer` for all platforms.
- [x] Implement `MysqlExceptionTransformer` and `PostgresExceptionTransformer`.
- [x] Wire transformers into `DivaDatabaseImpl`.
- [ ] Verify error propagation in repositories.

## Future Tasks
- [ ] Implement business logic in feature repositories.
- [ ] Implement ViewModels and UI logic for new feature modules (Permissions, Devices).
- [ ] Complete network integration using `DivaClient`.
