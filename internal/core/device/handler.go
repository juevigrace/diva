package device

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/middlewares"
	"github.com/juevigrace/diva-server/internal/models"
	"github.com/juevigrace/diva-server/internal/models/responses"
)

type DeviceHandler struct {
	repo *DeviceRepo
}

func NewDeviceHandler(repo *DeviceRepo) *DeviceHandler {
	return &DeviceHandler{repo: repo}
}

func (h *DeviceHandler) UserRoutes(r chi.Router) {
	r.Route("/devices", func(d chi.Router) {
		d.Use(middlewares.RequireResourceOwner(
			&middlewares.RequireOwnerParams{
				UrlParams: []string{"uid"},
				Perms:     []models.PermissionAction{models.PERMISSION_DEVICES_READ},
			},
			func(ctx context.Context, reqid uuid.UUID, resParams []string) (map[string]any, bool) {
				resid, err := uuid.Parse(resParams[0])
				if err != nil {
					return nil, false
				}
				if reqid != resid {
					return nil, false
				}
				return map[string]any{"uid": resid}, true
			},
		))
		d.Get("/", h.listUserDevices)
		d.Get("/{did}", h.getUserDevice)
		d.Delete("/{did}", h.deleteUserDevice)
	})
}

func (h *DeviceHandler) listDevices(w http.ResponseWriter, r *http.Request) {
	devices, err := h.repo.ListDevices(r.Context())
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	res := make([]*responses.DeviceResponse, len(devices))
	for i, d := range devices {
		res[i] = d.Response()
	}

	responses.WriteJSON(w, responses.RespondOk(res, "devices retrieved"))
}

func (h *DeviceHandler) listUserDevices(w http.ResponseWriter, r *http.Request) {
	uid, err := middlewares.GetUUIDFromURL(r, "uid")
	if err != nil {
		responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
		return
	}

	devices, err := h.repo.ListUserDevices(r.Context(), uid)
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	res := make([]*responses.UserDeviceResponse, len(devices))
	for i, d := range devices {
		res[i] = d.Response()
	}

	responses.WriteJSON(w, responses.RespondOk(res, "user devices retrieved"))
}

func (h *DeviceHandler) getUserDevice(w http.ResponseWriter, r *http.Request) {
	uid, err := middlewares.GetUUIDFromURL(r, "uid")
	if err != nil {
		responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
		return
	}

	did, err := middlewares.GetUUIDFromURL(r, "did")
	if err != nil {
		responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
		return
	}

	ud, err := h.repo.GetUserDevice(r.Context(), uid, did)
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	responses.WriteJSON(w, responses.RespondOk(ud.Response(), "user device retrieved"))
}

func (h *DeviceHandler) deleteUserDevice(w http.ResponseWriter, r *http.Request) {
	uid, err := middlewares.GetUUIDFromURL(r, "uid")
	if err != nil {
		responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
		return
	}

	did, err := middlewares.GetUUIDFromURL(r, "did")
	if err != nil {
		responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
		return
	}

	if err := h.repo.DeleteUserDevice(r.Context(), uid, did); err != nil {
		responses.HandleReqError(w, err)
		return
	}

	responses.WriteJSON(w, responses.RespondOk(nil, "user device deleted"))
}
