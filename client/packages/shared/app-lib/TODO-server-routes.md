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
| GET    | /user/check/username/{username} | public                                      |                                               | todo   |
| GET    | /user/check/email/{email}       | public                                      |                                               | todo   |
| GET    | /user                           | session, verified, ADMIN/MOD                | list all users                               | todo   |
| POST   | /user                           | session, verified, ADMIN/MOD                | `CreateUserDto`                              | todo   |
| GET    | /user/{uid}                     | session                                     |                                               | todo   |
| PATCH  | /user/{uid}/email               | session, verified, owner + `USERS_EMAIL_WRITE`   | `UpdateEmailDto`                        | todo   |
| PATCH  | /user/{uid}/phone               | session, verified, owner + `USERS_PHONE_WRITE`   | `UpdatePhoneNumberDto`                  | todo   |
| PATCH  | /user/{uid}/username            | session, verified, owner + `USERS_USERNAME_WRITE`| `UpdateUsernameDto`                   | todo   |
| PATCH  | /user/{uid}/password            | session, verified, owner + `USERS_PASSWORD_WRITE`| password update                        | todo   |
| PATCH  | /user/{uid}/role                | session, verified, ADMIN/MOD + `USERS_ROLE_WRITE` | `UpdateRoleDto`                      | todo   |
| PATCH  | /user/{uid}/restore             | session, verified, ADMIN/MOD + `USERS_RESTORE_WRITE` |                                   | todo   |
| DELETE | /user/{uid}                     | session, verified, owner + `USERS_WRITE`    | soft delete                                  | todo   |
| DELETE | /user/{uid}/forever             | session, verified, owner + `USERS_WRITE`    | hard delete                                  | todo   |
| GET    | /user/{uid}/status              | session, verified, owner                    | `UserStateResponse`                          | todo   |
| POST   | /user/{uid}/status/ping         | session, verified, owner                    |                                               | todo   |
| PATCH  | /user/{uid}/status/verified     | session, verified, ADMIN/MOD + `USERS_VERIFIED_WRITE` | `UpdateVerifiedDto`              | todo   |
| PUT    | /user/{uid}/status              | session, verified, ADMIN/MOD + `USERS_WRITE`| `UpdateUserStatusDto`                        | todo   |
| GET    | /user/{uid}/actions             | session, verified, owner + `ACTIONS_READ`   |                                               | todo   |
| GET    | /user/actions/{aid}             | session, verified, owner-by-action + `ACTIONS_READ` |                               | todo   |
| DELETE | /user/actions/{aid}             | session, verified, ADMIN/MOD + `ACTIONS_WRITE` |                                          | todo   |
| GET    | /user/{uid}/permissions         | session, verified, owner + `USER_PERMISSIONS_READ` |                                 | todo   |
| GET    | /user/{uid}/permissions/{pid}   | session, verified, owner + `USER_PERMISSIONS_READ` |                                 | todo   |
| POST   | /user/{uid}/permissions         | session, verified, ADMIN/MOD + `USER_PERMISSIONS_WRITE` | `CreateUserPermissionDto`     | todo   |
| PUT    | /user/{uid}/permissions/{pid}   | session, verified, ADMIN/MOD, grantedBy-owner + `USER_PERMISSIONS_WRITE` | `UpdateUserPermissionDto` | todo   |
| DELETE | /user/{uid}/permissions/{pid}   | session, verified, ADMIN/MOD, grantedBy-owner + `USER_PERMISSIONS_WRITE` |        | todo   |
| GET    | /user/{uid}/preferences         | session, verified, owner + `USER_PERMISSIONS_READ` | `UserPreferencesResponse`            | todo   |
| POST   | /user/{uid}/preferences         | session, verified, owner + `USERS_PREFERENCES_WRITE` | `CreateUserPreferencesDto`     | todo   |
| GET    | /user/preferences/{pid}         | session, verified, owner-by-pref + `USERS_PREFERENCES_READ` |                       | todo   |
| PUT    | /user/preferences/{pid}         | session, verified, owner-by-pref + `USERS_PREFERENCES_WRITE` | `UpdateUserPreferencesDto` | todo   |
| GET    | /user/{uid}/profile             | session, verified (**any verified session, NO owner check**) | `UserProfileResponse` | todo   |
| POST   | /user/{uid}/profile             | session, verified, owner + `USERS_PROFILE_WRITE` | `CreateProfileDto`                | todo   |
| PUT    | /user/{uid}/profile             | session, verified, owner + `USERS_PROFILE_WRITE` | `UpdateProfileDto`                | todo   |
| PATCH  | /user/{uid}/profile/avatar      | session, verified, owner + `USERS_PROFILE_WRITE` | multipart file upload          | todo   |
| GET    | /user/{uid}/sessions            | session, verified, owner + `SESSIONS_READ`  |                                               | todo   |
| DELETE | /user/{uid}/sessions            | session, verified, owner + `SESSIONS_WRITE` | soft-delete all user sessions                | todo   |
| DELETE | /user/{uid}/sessions/close      | session, verified, ADMIN/MOD                |                                               | todo   |
| GET    | /user/{uid}/devices             | session, verified, owner + `DEVICES_READ`   |                                               | todo   |
| GET    | /user/{uid}/devices/{did}       | session, verified, owner + `DEVICES_READ`   |                                               | todo   |
| DELETE | /user/{uid}/devices/{did}       | session, verified, owner + `DEVICES_READ`   | (no write permission attached)               | todo   |

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