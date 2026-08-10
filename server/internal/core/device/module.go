package device

import (
	"github.com/go-chi/chi/v5"
	"github.com/juevigrace/diva-server/internal/middlewares"
	"github.com/juevigrace/diva-server/internal/models"
	"github.com/juevigrace/diva-server/storage"
)

type DeviceModule struct {
	Handler *DeviceHandler
	Repo    *DeviceRepo
}

func NewDeviceModule(store storage.DeviceStore) *DeviceModule {
	repo := NewDeviceRepo(store)
	return &DeviceModule{
		Handler: NewDeviceHandler(repo),
		Repo:    repo,
	}
}

func (m *DeviceModule) Routes(r chi.Router, sCall middlewares.SessionCall, uCall middlewares.UserCall) {
	r.Route("/devices", func(d chi.Router) {
		d.Use(middlewares.RequiresSession(sCall, uCall), middlewares.RequireVerified())

		d.Group(func(admin chi.Router) {
			admin.Use(middlewares.RequireRole(models.ROLE_ADMIN, models.ROLE_MODERATOR))
			admin.Get("/", m.Handler.listDevices)
		})
	})
}
