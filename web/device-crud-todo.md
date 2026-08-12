# Device CRUD — Remaining Work

## Frontend (`pages/`)

1. **Fix route mismatch** — Frontend proxies to `/api/user/{uid}/devices` but backend only exposes `GET /api/devices`. Update the proxy URL and `DevicesManager` fetch to match the backend route.

2. **Add device detail view** — `repo.GetByID` exists on backend but has no HTTP handler. Wire up a detail endpoint + frontend page/component.

3. **Add Create device UI** — No standalone create endpoint exists. Add create button/modal in `DevicesManager` and a frontend proxy route for `POST /api/devices`.

4. **Add Rename/Update device UI** — No update endpoint exists. Add inline edit or rename dialog in `DevicesManager` and frontend proxy for `PUT /api/devices/{id}`.

5. **Add Delete device UI** — `DeleteUserDevice` exists in store but is unused. Add delete button with confirmation in `DevicesManager` and proxy for `DELETE /api/devices/{id}`.

6. **Add device request DTOs** to `lib/diva-types/src/device/` — Create/Update payload types (e.g. `CreateDeviceDto`, `UpdateDeviceDto`).

## Backend (`server/`)

7. **Add `POST /api/devices` handler** — Use existing `repo.Create`.

8. **Add `GET /api/devices/{id}` handler** — Use existing `repo.GetByID`.

9. **Add `PUT /api/devices/{id}` handler** — Requires new `repo.Update` method.

10. **Add `DELETE /api/devices/{id}` handler** — Requires new `repo.Delete` + exposing `DeleteUserDevice`.
