package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type Device struct {
	ID        uuid.UUID
	Name      string
	CreatedAt int64
	UpdatedAt int64
}

func (d *Device) Response() *responses.DeviceResponse {
	return &responses.DeviceResponse{
		ID:        d.ID.String(),
		Name:      d.Name,
		CreatedAt: d.CreatedAt,
		UpdatedAt: d.UpdatedAt,
	}
}

func DeviceFromDB(row *storage.DivaDevice) *Device {
	return &Device{
		ID:        row.ID,
		Name:      row.Name,
		CreatedAt: row.CreatedAt,
		UpdatedAt: row.UpdatedAt,
	}
}
