package session

import (
	"context"
	"net/http"

	"github.com/go-chi/chi/v5"
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/middlewares"
	"github.com/juevigrace/diva-server/internal/models"
	"github.com/juevigrace/diva-server/internal/models/responses"
)

type SessionHandler struct {
	sRepo *SessionRepo
}

func NewSessionHandler(sRepo *SessionRepo) *SessionHandler {
	return &SessionHandler{sRepo: sRepo}
}

func (h *SessionHandler) UserRoutes(r chi.Router) {
	r.Route("/sessions", func(s chi.Router) {
		s.With(middlewares.RequireResourceOwner(
			&middlewares.RequireOwnerParams{
				UrlParams: []string{"uid"},
				Perms:     []models.PermissionAction{models.PERMISSION_SESSIONS_READ},
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
		)).Get("/", h.listByUser)

		s.With(middlewares.RequireResourceOwner(
			&middlewares.RequireOwnerParams{
				UrlParams: []string{"uid"},
				Perms:     []models.PermissionAction{models.PERMISSION_SESSIONS_WRITE},
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
		)).Delete("/", h.softDelete)

		s.With(middlewares.RequireRole(models.ROLE_ADMIN, models.ROLE_MODERATOR)).
			Delete("/close", h.closeAllByUser)
	})
}

func (h *SessionHandler) listByUser(w http.ResponseWriter, r *http.Request) {
	rc, err := middlewares.GetRequestContext(r.Context())
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	uid, ok := rc.Cache["uid"].(uuid.UUID)
	if !ok {
		uid, err = middlewares.GetUUIDFromURL(r, "uid")
		if err != nil {
			responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
			return
		}
	}

	sessions, err := h.sRepo.GetByUser(r.Context(), uid)
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	res := make([]*responses.SessionResponse, len(sessions))
	for i, s := range sessions {
		res[i] = s.Response()
	}

	responses.WriteJSON(w, responses.RespondOk(res, "sessions retrieved"))
}

func (h *SessionHandler) getByID(w http.ResponseWriter, r *http.Request) {
	rc, err := middlewares.GetRequestContext(r.Context())
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	session, ok := rc.Cache["sid"].(*models.Session)
	if !ok {
		sid, err := middlewares.GetUUIDFromURL(r, "sid")
		if err != nil {
			responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
			return
		}

		session, err = h.sRepo.GetByID(r.Context(), sid)
		if err != nil {
			responses.HandleReqError(w, err)
			return
		}
	}

	responses.WriteJSON(w, responses.RespondOk(session.Response(), "session retrieved"))
}

func (h *SessionHandler) close(w http.ResponseWriter, r *http.Request) {
	rc, err := middlewares.GetRequestContext(r.Context())
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	var sid uuid.UUID
	session, ok := rc.Cache["sid"].(*models.Session)
	if !ok {
		sid, err = middlewares.GetUUIDFromURL(r, "sid")
		if err != nil {
			responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
			return
		}
	} else {
		sid = session.ID
	}

	if session.Status != models.SESSION_ACTIVE {
		responses.WriteJSON(w, responses.RespondOk(nil, "session closed"))
		return
	}

	if err := h.sRepo.Close(r.Context(), sid); err != nil {
		responses.HandleReqError(w, err)
		return
	}

	responses.WriteJSON(w, responses.RespondOk(nil, "session closed"))
}

func (h *SessionHandler) listAll(w http.ResponseWriter, r *http.Request) {
	sessions, err := h.sRepo.ListAll(r.Context())
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	res := make([]*responses.SessionResponse, len(sessions))
	for i, s := range sessions {
		res[i] = s.Response()
	}

	responses.WriteJSON(w, responses.RespondOk(res, "sessions retrieved"))
}

func (h *SessionHandler) softDelete(w http.ResponseWriter, r *http.Request) {
	rc, err := middlewares.GetRequestContext(r.Context())
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	uid, ok := rc.Cache["uid"].(uuid.UUID)
	if !ok {
		uid, err = middlewares.GetUUIDFromURL(r, "uid")
		if err != nil {
			responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
			return
		}
	}

	sessions, err := h.sRepo.GetByUser(r.Context(), uid)
	if err != nil {
		responses.HandleReqError(w, err)
		return
	}

	for _, s := range sessions {
		if s.Status != models.SESSION_ACTIVE {
			if err := h.sRepo.SoftDelete(r.Context(), s.ID); err != nil {
				responses.HandleReqError(w, err)
				return
			}
		}
	}

	responses.WriteJSON(w, responses.RespondOk(nil, "history cleared"))
}

func (h *SessionHandler) closeAllByUser(w http.ResponseWriter, r *http.Request) {
	uid, err := middlewares.GetUUIDFromURL(r, "uid")
	if err != nil {
		responses.WriteJSON(w, responses.RespondBadRequest(nil, err.Error()))
		return
	}

	if err := h.sRepo.CloseAllByUser(r.Context(), uid); err != nil {
		responses.HandleReqError(w, err)
		return
	}

	responses.WriteJSON(w, responses.RespondOk(nil, "all sessions closed"))
}

func (h *SessionHandler) closeExpired(w http.ResponseWriter, r *http.Request) {
	if err := h.sRepo.CloseExpired(r.Context()); err != nil {
		responses.HandleReqError(w, err)
		return
	}

	responses.WriteJSON(w, responses.RespondOk(nil, "expired sessions closed"))
}

func (h *SessionHandler) deleteSessionsForever(w http.ResponseWriter, r *http.Request) {
	if err := h.sRepo.DeleteSessionsForever(r.Context()); err != nil {
		responses.HandleReqError(w, err)
		return
	}

	responses.WriteJSON(w, responses.RespondOk(nil, "sessions permanently deleted"))
}
