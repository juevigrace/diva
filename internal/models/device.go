package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/storage"
)

type Device struct {
	ID        uuid.UUID
	Name      string
	CreatedAt int64
	UpdatedAt int64
}

func DeviceFromDB(row *storage.DivaDevice) *Device {
	return &Device{
		ID:        row.ID,
		Name:      row.Name,
		CreatedAt: row.CreatedAt,
		UpdatedAt: row.UpdatedAt,
	}
}
