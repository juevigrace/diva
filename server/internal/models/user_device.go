package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type UserDevice struct {
	UserID    uuid.UUID
	Device    Device
	CreatedAt int64
	UpdatedAt int64
}

func UserDeviceFromDB(row *storage.DivaUserDevice, device *Device) *UserDevice {
	return &UserDevice{
		UserID:    row.UserID,
		Device:    *device,
		CreatedAt: row.CreatedAt,
		UpdatedAt: row.UpdatedAt,
	}
}

func (ud *UserDevice) Response() *responses.UserDeviceResponse {
	return &responses.UserDeviceResponse{
		UserID:     ud.UserID.String(),
		DeviceID:   ud.Device.ID.String(),
		DeviceName: ud.Device.Name,
		CreatedAt:  ud.CreatedAt,
		UpdatedAt:  ud.UpdatedAt,
	}
}
