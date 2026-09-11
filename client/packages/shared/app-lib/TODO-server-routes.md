# Server Route System (master branch)

Route map of the Go server (`master:server/server/server.go` + `internal/core/*/module.go`).
Goal: reconcile/implement the shared-library API clients against these routes.

## Conventions

- All API routes live under `/api`.
- Rate limit: per-IP 60 req/min on `/` (covers `/api/*` and `/status`).
- Auth: JWT Bearer via `Authorization` header (`middlewares.RequiresSession`).
  Shortnames below:
  - `session` = requires a valid active session (JWT)
  - `verified` = `RequireVerified` (user must be verified)
  - `ADMIN/MOD` = `RequireRole(ROLE_ADMIN | ROLE_MODERATOR)`
  - `ADMIN` = `RequireRole(ROLE_ADMIN)` only
  - `owner` = `RequireResourceOwner` (requesting user must own the URL resource; some also require a `PermissionAction`)
- Fine-grained access uses `RequirePermission(PermissionAction)`.
- DTO/response model names reference `diva-lib-models-api`.
- Status: `done` = API client implemented in app-lib; `todo` = not yet implemented.

## Root (no `/api` prefix)

| Method | Path        | Auth            | Body / Notes                      | Status |
|--------|-------------|-----------------|-----------------------------------|--------|
| GET    | /health     | public          | returns `{ "status": "ok" }`      | todo   |
| GET    | /status     | session, ADMIN/MOD | DB health                        | todo   |
| GET    | /uploads    | public          | static file server (uploads dir)  | todo   |
| *      | (404)       | public          | JSON "Route not found"            | todo   |

## /api/user

| Method | Path                            | Auth                                        | Body / Notes                                  | Status |
|--------|---------------------------------|---------------------------------------------|-----------------------------------------------|--------|
| GET    | /user/check/username/{username} | public                                      |                                               | done   |
| GET    | /user/check/email/{email}       | public                                      |                                               | done   |
| GET    | /user                           | session, verified, ADMIN/MOD                | list all users                               | done   |
| POST   | /user                           | session, verified, ADMIN/MOD                | `CreateUserDto`                              | done   |
| GET    | /user/{uid}                     | session                                     |                                               | done   |
| PATCH  | /user/{uid}/email               | session, verified, owner + `USERS_EMAIL_WRITE`   | `UpdateEmailDto`                        | done   |
| PATCH  | /user/{uid}/phone               | session, verified, owner + `USERS_PHONE_WRITE`   | `UpdatePhoneNumberDto`                  | done   |
| PATCH  | /user/{uid}/username            | session, verified, owner + `USERS_USERNAME_WRITE`| `UpdateUsernameDto`                   | done   |
| PATCH  | /user/{uid}/password            | session, verified, owner + `USERS_PASSWORD_WRITE`| password update                        | done   |
| PATCH  | /user/{uid}/role                | session, verified, ADMIN/MOD + `USERS_ROLE_WRITE` | `UpdateRoleDto`                      | done   |
| PATCH  | /user/{uid}/restore             | session, verified, ADMIN/MOD + `USERS_RESTORE_WRITE` |                                   | done   |
| DELETE | /user/{uid}                     | session, verified, owner + `USERS_WRITE`    | soft delete                                  | done   |
| DELETE | /user/{uid}/forever             | session, verified, owner + `USERS_WRITE`    | hard delete                                  | done   |
| GET    | /user/{uid}/status              | session, verified, owner                    | `UserStateResponse`                          | done   |
| POST   | /user/{uid}/status/ping         | session, verified, owner                    |                                               | done   |
| PATCH  | /user/{uid}/status/verified     | session, verified, ADMIN/MOD + `USERS_VERIFIED_WRITE` | `UpdateVerifiedDto`              | done   |
| PUT    | /user/{uid}/status              | session, verified, ADMIN/MOD + `USERS_WRITE`| `UpdateUserStatusDto`                        | done   |
| GET    | /user/{uid}/actions             | session, verified, owner + `ACTIONS_READ`   |                                               | done   |
| GET    | /user/actions/{aid}             | session, verified, owner-by-action + `ACTIONS_READ` |                               | done   |
| DELETE | /user/actions/{aid}             | session, verified, ADMIN/MOD + `ACTIONS_WRITE` |                                          | done   |
| GET    | /user/{uid}/permissions         | session, verified, owner + `USER_PERMISSIONS_READ` |                                 | done   |
| GET    | /user/{uid}/permissions/{pid}   | session, verified, owner + `USER_PERMISSIONS_READ` |                                 | done   |
| POST   | /user/{uid}/permissions         | session, verified, ADMIN/MOD + `USER_PERMISSIONS_WRITE` | `CreateUserPermissionDto`     | done   |
| PUT    | /user/{uid}/permissions/{pid}   | session, verified, ADMIN/MOD, grantedBy-owner + `USER_PERMISSIONS_WRITE` | `UpdateUserPermissionDto` | done   |
| DELETE | /user/{uid}/permissions/{pid}   | session, verified, ADMIN/MOD, grantedBy-owner + `USER_PERMISSIONS_WRITE` |        | done   |
| GET    | /user/{uid}/preferences         | session, verified, owner + `USER_PERMISSIONS_READ` | `UserPreferencesResponse`            | done   |
| POST   | /user/{uid}/preferences         | session, verified, owner + `USERS_PREFERENCES_WRITE` | `CreateUserPreferencesDto`     | done   |
| GET    | /user/preferences/{pid}         | session, verified, owner-by-pref + `USERS_PREFERENCES_READ` |                       | done   |
| PUT    | /user/preferences/{pid}         | session, verified, owner-by-pref + `USERS_PREFERENCES_WRITE` | `UpdateUserPreferencesDto` | done   |
| GET    | /user/{uid}/profile             | session, verified (**any verified session, NO owner check**) | `UserProfileResponse` | done   |
| POST   | /user/{uid}/profile             | session, verified, owner + `USERS_PROFILE_WRITE` | `CreateProfileDto`                | done   |
| PUT    | /user/{uid}/profile             | session, verified, owner + `USERS_PROFILE_WRITE` | `UpdateProfileDto`                | done   |
| PATCH  | /user/{uid}/profile/avatar      | session, verified, owner + `USERS_PROFILE_WRITE` | multipart file upload          | done   |
| GET    | /user/{uid}/sessions            | session, verified, owner + `SESSIONS_READ`  |                                               | done   |
| DELETE | /user/{uid}/sessions            | session, verified, owner + `SESSIONS_WRITE` | soft-delete all user sessions                | done   |
| DELETE | /user/{uid}/sessions/close      | session, verified, ADMIN/MOD                |                                               | done   |
| GET    | /user/{uid}/devices             | session, verified, owner + `DEVICES_READ`   |                                               | done   |
| GET    | /user/{uid}/devices/{did}       | session, verified, owner + `DEVICES_READ`   |                                               | done   |
| DELETE | /user/{uid}/devices/{did}       | session, verified, owner + `DEVICES_READ`   | (no write permission attached)               | done   |

## /api/sessions (all require session + verified)

| Method | Path                     | Auth                  | Body / Notes            | Status |
|--------|--------------------------|-----------------------|-------------------------|--------|
| GET    | /sessions                | ADMIN/MOD             | list all                | done   |
| DELETE | /sessions/close          | ADMIN/MOD             | close expired           | done   |
| DELETE | /sessions                | ADMIN/MOD             | delete sessions forever | done   |
| GET    | /sessions/{sid}          | owner-by-sid + `SESSIONS_READ`  | `SessionResponse` | done   |
| DELETE | /sessions/{sid}/close    | owner-by-sid + `SESSIONS_WRITE` |                   | done   |

## /api/auth

| Method | Path                              | Auth     | Body / Notes                          | Status |
|--------|-----------------------------------|----------|---------------------------------------|--------|
| POST   | /auth/signIn                      | public   | `SignInDto`                           | done   |
| POST   | /auth/signUp                      | public   | `SignUpDto`                           | done   |
| POST   | /auth/signOut                     | session  |                                       | done   |
| POST   | /auth/ping                        | session  |                                       | done   |
| POST   | /auth/refresh                     | session  | `SessionDataDto`                      | done   |
| POST   | /auth/forgot/password/confirm     | public   | `ForgotPasswordConfirmDto` (token + new password) | done   |

## /api/permissions (all require session + verified)

| Method | Path                     | Auth                       | Body / Notes               | Status |
|--------|--------------------------|----------------------------|----------------------------|--------|
| GET    | /permissions             | ADMIN/MOD + `PERMISSIONS_READ`  | list                  | done   |
| GET    | /permissions/{pid}       | ADMIN/MOD + `PERMISSIONS_READ`  | `PermissionResponse`  | done   |
| PUT    | /permissions/{pid}       | ADMIN/MOD + `PERMISSIONS_WRITE` | `UpdatePermissionDto` | done   |
| PATCH  | /permissions/{pid}/level | ADMIN only                  | `UpdatePermissionRoleLevelDto` | done   |

## /api/verification

| Method | Path                 | Auth   | Body / Notes                           | Status |
|--------|----------------------|--------|----------------------------------------|--------|
| POST   | /verification/request| public | `RequestActionVerificationDto` (email, action) | done   |
| POST   | /verification        | public | `VerifyActionDto` (actionId, token)    | done   |

## /api/devices

| Method | Path      | Auth                    | Body / Notes | Status |
|--------|-----------|-------------------------|--------------|--------|
| GET    | /devices  | session, verified, ADMIN/MOD | list all devices | done   |

## Notes for client implementation

- `PATCH /user/{uid}/profile/avatar` is a multipart upload.
- `USER_PERMISSIONS_READ` is also used for `GET /user/{uid}/preferences` (not a preferences-specific action).
- `GET /user/{uid}/profile` intentionally has no ownership check — any verified session may read it.
- All `/user/{uid}*` sub-resources (actions/permissions/preferences/profile/sessions/devices) are nested under the `{uid}` path param; global-scoped variants exist for actions/preferences under `/user/actions|preferences/{id}`.