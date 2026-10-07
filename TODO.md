# TODO: Reconcile repository patterns to Settings vocabulary + session scoping

Full plan details: `~/.local/share/opencode/plans/reconcile-repository-patterns.md`
Status: WAITING for execution command.

## Decisions (confirmed)
1. Rename all repos to Settings vocabulary: `observe()` / `get()` / `upsert()` / `delete()` / `sync()`, domain extras kept.
2. Only BULK per-user deletes keep `userId` (`deleteAllByUser`, `deleteCompletedByUser`, `resetSettings`, `UserPreferences.delete(userId)`); single-row deletes (`deleteResumePoint`, `UserDevices.delete`, `UserPermissions.delete`) become session-scoped.
3. New per-table cleanup deletes: leave as-is for now (no new SQL/storage/repo functions this pass).
4. Remove LibraryRepository forwarders `getRecent` / `getResumable` / `getFolders` / `sync`.
5. No trivial helper functions — inline them (e.g. `FolderViewModel.key`).
6. `state.update {}` works as-is — do not touch viewmodel state declarations.
7. Use session helpers: `withSession` (suspend), `observeSession` (Flow), `withSessionFlow` (one-shot).

## Execution steps (in order)

### A. Session renames
- [ ] `SessionRepository`: `getCurrent→get`, `getCurrentFlow→observe`, `save→upsert` (interface + impl)
- [ ] Ripples: `withSession(sessionRepository::getCurrent)` → `::get`, `observeSession(::getCurrentFlow)` → `::observe` in ~10 impls; AppViewModel 3 sites

### B. User-core repos (`user/user-core`)
- [ ] UserState: `getState(userId)→observe()`, `sync(userId)→sync()`, `save→upsert`; unwrap Option→plain in impl
- [ ] UserProfile: same shape; unwrap Option→plain
- [ ] UserPreferences: `getPreferences→observe()`, `sync→sync()`, `save→upsert`, `delete(id)→delete(userId)` (bulk keeps userId)
- [ ] UserPermissions: `getPermissions→observe()`, `getPermission(permissionId, userId)→getPermission(permissionId)`, `sync→sync()`, `save→upsert`, `delete(permissionId, userId)→delete(permissionId)` (session-scoped)
- [ ] UserActions: `getActions→observe()`, `getAction(userId, action)→getAction(action)`, `sync→sync()`, `save→upsert`, `delete(id)` unchanged
- [ ] UserDevices: rewrite `getDevices()`/`getDevice(id)` with observeSession + Option unwrap; fix `sync()` undefined `userId` → `session.userId`; fix `getDevice` empty-collect bug; `save→upsert`; `delete(userId, deviceId)→delete(deviceId)` (session-scoped)
- [ ] User: `getUsers()→observe()`, `getUser(id)→observe(id)` (keeps id), `save→upsert`; unwrap Option→plain
- [ ] Remove now-unused imports (Option, map, withSessionFlow where unused)

### D. Global repos (rename only, no session)
- [ ] Devices: `getDevices→observe()`, `getDevice(id)→observe(id)`, `save→upsert`
- [ ] Permissions: `getPermissions→observe()`, `getPermission(id)→observe(id)`, `save→upsert`
- [ ] Tag: `getTags→observe()`, `getTag(id)→observe(id)`, `save→upsert`
- [ ] Media: `getMedia→observe()`, `getMedia(id)→observe(id)`, `save→upsert` (tag link ops unchanged)
- [ ] MediaMetadata: `getMetadata(mediaId)→observe(mediaId)`; unwrap Option→plain in impl; `save→upsert`
- [ ] Collection: `getCollections→observe()`, `getCollection(id)→observe(id)`, `save→upsert`
- [ ] Server: `getServers→observe()`, `getServer(id)→observe(id)`, `save→upsert`
- [ ] Mix: `getMix(id)→get(id)`, `getMixFlow→observe(id)`, `save→upsert`
- [ ] Playlist: `getPlaylist→get(id)`, `getPlaylistFlow→observe(id)`, `save→upsert`
- [ ] Session extra: `getSessions()` keep (current-session flow is `observe()`)
- [ ] Auth/Verification/Onboarding/Search: no changes

### C. App repos (session-scope + rename)
- [ ] Folder: `getFolders(userId)→observe()`, `getFolder(id)→observe(id)`, `getRoots(userId)→observeRoots()`, `getChildren(userId,parentId)→getChildren(parentId)`, `save→upsert`; `deleteAllByUser(userId)` unchanged; add SessionRepository dep
- [ ] Library: remove `getRecent`/`getResumable`/`getFolders`/`sync` (interface+impl); `getFavorites(userId)→observe()`, `toggleFavorite/removeFavorite/isFavorite(userId, mediaId)→(mediaId)`; add SessionRepository dep, drop unused deps
- [ ] Player: `getResumePoint(userId,mediaId)→getResumePoint(mediaId)`, `deleteResumePoint→deleteResumePoint(mediaId)` (session), `getResumableByUser→getResumable()`, `...Flow→observeResumable()`, `getRecentByUser(userId,limit)→getRecent(limit)`, `countCompletedByUser→countCompleted()`, `getSetting(userId)→getSetting()`, `getSettingFlow→observeSetting()`, `saveSetting→upsertSetting`, `saveResumePoint→upsertResumePoint`; `deleteCompletedByUser`/`resetSettings(userId)` unchanged; add SessionRepository dep
- [ ] ProfileRepository interface: `Flow<Result<Profile?>>→Flow<Result<Profile>>`; `observeProfile()→observe()`
- [ ] ProfileRepositoryImpl: `getState(session.userId)→observe()`, `getProfile(session.userId)→observe()`, `getPreferences(session.userId)→observe()`, `getDevices()→observe()`, `getUser(session.userId)→observe(session.userId)`
- [ ] SearchRepositoryImpl: `folderRepository.getFolders(userId)→observe()`, `mediaRepository.getMedia()→observe()`, `collectionRepository.getCollections()→observe()`; keep `mediaMatches`
- [ ] SettingsRepositoryImpl: ripples only (`::getCurrent→::get`, `::getCurrentFlow→::observe`)

### E. Callers / error fixes
- [ ] AppViewModel: `settingsRepository.get(session.userId)→get()`, `save→upsert` (user/devices/session ×3), `getUser(...).isSome→observe(...)!=null`, `getCurrentFlow().isSome→observe()!=null`, `seedSession get/upsert`, `observeSettings` drop getCurrent wrapper → `settingsRepository.observe().collect { publish(it) }`
- [ ] SettingsViewModel: fix `result.map` discard → `state.update { it.copy(settings = Option.of(result.getOrNull())) }`
- [ ] FolderViewModel: inline `key()` (5 sites), remove helper

### NOT doing
- New `delete(userId)` SQL/storage/repo functions
- `Repository.scope get()` nit
- `isLoading` cosmetic
- Profile→User merge
- `mediaMatches` / `update`/`updateDraft` removal
- Viewmodel state pattern changes

## Verification
- [ ] Compile framework → app-lib → apps/diva (per build), fix errors iteratively
- [ ] Grep leftover old names: `getCurrentFlow|getCurrentSession|\.save\(|getState\(|getProfile\(|getPreferences\(|getFolders\(|getFavorites\(|getResumableByUser|getSettingFlow|getMetadata\(|isSome`
- [ ] Final compile clean
