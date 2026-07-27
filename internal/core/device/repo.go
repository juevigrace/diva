package device

import (
	"context"

	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models"
	"github.com/juevigrace/diva-server/storage"
)

type DeviceRepo struct {
	store storage.DeviceStore
}

func NewDeviceRepo(store storage.DeviceStore) *DeviceRepo {
	return &DeviceRepo{store: store}
}

func (r *DeviceRepo) GetByName(ctx context.Context, name string) (*models.Device, error) {
	row, err := r.store.GetDeviceByName(ctx, name)
	if err != nil {
		return nil, err
	}
	return models.DeviceFromDB(row), nil
}

func (r *DeviceRepo) Create(ctx context.Context, name string) (*models.Device, error) {
	id := uuid.New()
	if err := r.store.CreateDevice(ctx, &storage.CreateDeviceParams{
		ID:   id,
		Name: name,
	}); err != nil {
		return nil, err
	}
	return r.GetByID(ctx, id)
}

func (r *DeviceRepo) GetByID(ctx context.Context, id uuid.UUID) (*models.Device, error) {
	row, err := r.store.GetDeviceByID(ctx, id)
	if err != nil {
		return nil, err
	}
	return models.DeviceFromDB(row), nil
}
