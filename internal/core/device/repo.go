package device

import (
	"context"
	"errors"

	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models"
	"github.com/juevigrace/diva-server/pkg/errs"
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
	if _, err := r.GetByName(ctx, name); err == nil {
		return nil, errs.ErrDeviceNameTaken
	} else if !errors.Is(err, errs.ErrDeviceNotFound) {
		return nil, err
	}

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

func (r *DeviceRepo) GetUserDevice(ctx context.Context, userID uuid.UUID, deviceID uuid.UUID) (*models.UserDevice, error) {
	row, err := r.store.GetUserDevice(ctx, userID, deviceID)
	if err != nil {
		return nil, err
	}
	dev, err := r.GetByID(ctx, row.DeviceID)
	if err != nil {
		return nil, err
	}
	return models.UserDeviceFromDB(row, dev), nil
}

func (r *DeviceRepo) CreateUserDevice(ctx context.Context, userID uuid.UUID, deviceID uuid.UUID) error {
	if _, err := r.GetUserDevice(ctx, userID, deviceID); err == nil {
		return errs.ErrUserDeviceExists
	} else if !errors.Is(err, errs.ErrUserDeviceNotFound) {
		return err
	}

	return r.store.CreateUserDevice(ctx, &storage.CreateUserDeviceParams{
		UserID:   userID,
		DeviceID: deviceID,
	})
}

func (r *DeviceRepo) ListDevices(ctx context.Context) ([]*models.Device, error) {
	rows, err := r.store.ListAllDevices(ctx)
	if err != nil {
		return nil, err
	}

	devices := make([]*models.Device, len(rows))
	for i := range rows {
		devices[i] = models.DeviceFromDB(&rows[i])
	}
	return devices, nil
}

func (r *DeviceRepo) ListUserDevices(ctx context.Context, userID uuid.UUID) ([]*models.UserDevice, error) {
	rows, err := r.store.ListUserDevices(ctx, userID)
	if err != nil {
		return nil, err
	}

	devices := make([]*models.UserDevice, len(rows))
	for i, ud := range rows {
		dev, err := r.GetByID(ctx, ud.DeviceID)
		if err != nil {
			return nil, err
		}
		devices[i] = models.UserDeviceFromDB(&ud, dev)
	}
	return devices, nil
}

func (r *DeviceRepo) DeleteUserDevice(ctx context.Context, userID uuid.UUID, deviceID uuid.UUID) error {
	return r.store.DeleteUserDevice(ctx, userID, deviceID)
}
