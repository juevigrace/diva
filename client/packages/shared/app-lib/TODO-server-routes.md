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

## Root (no `/api` prefix)

| Method | Path        | Auth            | Body / Notes                      |
|--------|-------------|-----------------|-----------------------------------|
| GET    | /health     | public          | returns `{ "status": "ok" }`      |
| GET    | /status     | session, ADMIN/MOD | DB health                        |
| GET    | /uploads    | public          | static file server (uploads dir)  |
| *      | (404)       | public          | JSON "Route not found"            |

## /api/user

| Method | Path                            | Auth                                        | Body / Notes                                  |
|--------|---------------------------------|---------------------------------------------|-----------------------------------------------|
| GET    | /user/check/username/{username} | public                                      |                                               |
| GET    | /user/check/email/{email}       | public                                      |                                               |
| GET    | /user                           | session, verified, ADMIN/MOD                | list all users                               |
| POST   | /user                           | session, verified, ADMIN/MOD                | `CreateUserDto`                              |
| GET    | /user/{uid}                     | session                                     |                                               |
| PATCH  | /user/{uid}/email               | session, verified, owner + `USERS_EMAIL_WRITE`   | `UpdateEmailDto`                        |
| PATCH  | /user/{uid}/phone               | session, verified, owner + `USERS_PHONE_WRITE`   | `UpdatePhoneNumberDto`                  |
| PATCH  | /user/{uid}/username            | session, verified, owner + `USERS_USERNAME_WRITE`| `UpdateUsernameDto`                   |
| PATCH  | /user/{uid}/password            | session, verified, owner + `USERS_PASSWORD_WRITE`| password update                        |
| PATCH  | /user/{uid}/role                | session, verified, ADMIN/MOD + `USERS_ROLE_WRITE` | `UpdateRoleDto`                      |
| PATCH  | /user/{uid}/restore             | session, verified, ADMIN/MOD + `USERS_RESTORE_WRITE` |                                   |
| DELETE | /user/{uid}                     | session, verified, owner + `USERS_WRITE`    | soft delete                                  |
| DELETE | /user/{uid}/forever             | session, verified, owner + `USERS_WRITE`    | hard delete                                  |
| GET    | /user/{uid}/status              | session, verified, owner                    | `UserStateResponse`                          |
| POST   | /user/{uid}/status/ping         | session, verified, owner                    |                                               |
| PATCH  | /user/{uid}/status/verified     | session, verified, ADMIN/MOD + `USERS_VERIFIED_WRITE` | `UpdateVerifiedDto`              |
| PUT    | /user/{uid}/status              | session, verified, ADMIN/MOD + `USERS_WRITE`| `UpdateUserStatusDto`                        |
| GET    | /user/{uid}/actions             | session, verified, owner + `ACTIONS_READ`   |                                               |
| GET    | /user/actions/{aid}             | session, verified, owner-by-action + `ACTIONS_READ` |                               |
| DELETE | /user/actions/{aid}             | session, verified, ADMIN/MOD + `ACTIONS_WRITE` |                                          |
| GET    | /user/{uid}/permissions         | session, verified, owner + `USER_PERMISSIONS_READ` |                                 |
| GET    | /user/{uid}/permissions/{pid}   | session, verified, owner + `USER_PERMISSIONS_READ` |                                 |
| POST   | /user/{uid}/permissions         | session, verified, ADMIN/MOD + `USER_PERMISSIONS_WRITE` | `CreateUserPermissionDto`     |
| PUT    | /user/{uid}/permissions/{pid}   | session, verified, ADMIN/MOD, grantedBy-owner + `USER_PERMISSIONS_WRITE` | `UpdateUserPermissionDto` |
| DELETE | /user/{uid}/permissions/{pid}   | session, verified, ADMIN/MOD, grantedBy-owner + `USER_PERMISSIONS_WRITE` |        |
| GET    | /user/{uid}/preferences         | session, verified, owner + `USER_PERMISSIONS_READ` | `UserPreferencesResponse`            |
| POST   | /user/{uid}/preferences         | session, verified, owner + `USERS_PREFERENCES_WRITE` | `CreateUserPreferencesDto`     |
| GET    | /user/preferences/{pid}         | session, verified, owner-by-pref + `USERS_PREFERENCES_READ` |                       |
| PUT    | /user/preferences/{pid}         | session, verified, owner-by-pref + `USERS_PREFERENCES_WRITE` | `UpdateUserPreferencesDto` |
| GET    | /user/{uid}/profile             | session, verified (**any verified session, NO owner check**) | `UserProfileResponse` |
| POST   | /user/{uid}/profile             | session, verified, owner + `USERS_PROFILE_WRITE` | `CreateProfileDto`                |
| PUT    | /user/{uid}/profile             | session, verified, owner + `USERS_PROFILE_WRITE` | `UpdateProfileDto`                |
| PATCH  | /user/{uid}/profile/avatar      | session, verified, owner + `USERS_PROFILE_WRITE` | multipart file upload          |
| GET    | /user/{uid}/sessions            | session, verified, owner + `SESSIONS_READ`  |                                               |
| DELETE | /user/{uid}/sessions            | session, verified, owner + `SESSIONS_WRITE` | soft-delete all user sessions                |
| DELETE | /user/{uid}/sessions/close      | session, verified, ADMIN/MOD                |                                               |
| GET    | /user/{uid}/devices             | session, verified, owner + `DEVICES_READ`   |                                               |
| GET    | /user/{uid}/devices/{did}       | session, verified, owner + `DEVICES_READ`   |                                               |
| DELETE | /user/{uid}/devices/{did}       | session, verified, owner + `DEVICES_READ`   | (no write permission attached)               |

## /api/sessions (all require session + verified)

| Method | Path                     | Auth                  | Body / Notes            |
|--------|--------------------------|-----------------------|-------------------------|
| GET    | /sessions                | ADMIN/MOD             | list all                |
| DELETE | /sessions/close          | ADMIN/MOD             | close expired           |
| DELETE | /sessions                | ADMIN/MOD             | delete sessions forever |
| GET    | /sessions/{sid}          | owner-by-sid + `SESSIONS_READ`  | `SessionResponse` |
| DELETE | /sessions/{sid}/close    | owner-by-sid + `SESSIONS_WRITE` |                   |

## /api/auth

| Method | Path                              | Auth     | Body / Notes                          |
|--------|-----------------------------------|----------|---------------------------------------|
| POST   | /auth/signIn                      | public   | `SignInDto`                           |
| POST   | /auth/signUp                      | public   | `SignUpDto`                           |
| POST   | /auth/signOut                     | session  |                                       |
| POST   | /auth/ping                        | session  |                                       |
| POST   | /auth/refresh                     | session  | `SessionDataDto`                      |
| POST   | /auth/forgot/password/confirm     | public   | `ForgotPasswordConfirmDto` (token + new password) |

## /api/permissions (all require session + verified)

| Method | Path                     | Auth                       | Body / Notes               |
|--------|--------------------------|----------------------------|----------------------------|
| GET    | /permissions             | ADMIN/MOD + `PERMISSIONS_READ`  | list                  |
| GET    | /permissions/{pid}       | ADMIN/MOD + `PERMISSIONS_READ`  | `PermissionResponse`  |
| PUT    | /permissions/{pid}       | ADMIN/MOD + `PERMISSIONS_WRITE` | `UpdatePermissionDto` |
| PATCH  | /permissions/{pid}/level | ADMIN only                  | `UpdatePermissionRoleLevelDto` |

## /api/verification

| Method | Path                 | Auth   | Body / Notes                           |
|--------|----------------------|--------|----------------------------------------|
| POST   | /verification/request| public | `RequestActionVerificationDto` (email, action) |
| POST   | /verification        | public | `VerifyActionDto` (actionId, token)    |

## /api/devices

| Method | Path      | Auth                    | Body / Notes |
|--------|-----------|-------------------------|--------------|
| GET    | /devices  | session, verified, ADMIN/MOD | list all devices |

## Notes for client implementation

- `PATCH /user/{uid}/profile/avatar` is a multipart upload.
- `USER_PERMISSIONS_READ` is also used for `GET /user/{uid}/preferences` (not a preferences-specific action).
- `GET /user/{uid}/profile` intentionally has no ownership check — any verified session may read it.
- All `/user/{uid}*` sub-resources (actions/permissions/preferences/profile/sessions/devices) are nested under the `{uid}` path param; global-scoped variants exist for actions/preferences under `/user/actions|preferences/{id}`.