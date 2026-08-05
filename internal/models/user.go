package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type User struct {
	ID           uuid.UUID
	Username     string
	Email        string
	PhoneNumber  string
	PasswordHash string
	Role         Role
	State        *UserState
	CreatedAt    int64
	UpdatedAt    int64
	DeletedAt    *int64
	Profile      *UserProfile
	Devices      []UserDevice
	Actions      []UserAction
	Permissions  map[PermissionAction]UserPermission
	Preferences  *UserPreferences
}

func (u *User) Response() *responses.UserResponse {
	var state *responses.UserStateResponse
	if u.State != nil {
		state = &responses.UserStateResponse{
			Verified:     u.State.Verified,
			Status:       u.State.Status.String(),
			LastActiveAt: u.State.LastActiveAt,
		}
	}

	return &responses.UserResponse{
		ID:          u.ID.String(),
		Username:    u.Username,
		Email:       u.Email,
		PhoneNumber: u.PhoneNumber,
		Role:        u.Role.String(),
		State:       state,
		CreatedAt:   u.CreatedAt,
		UpdatedAt:   u.UpdatedAt,
		DeletedAt:   u.DeletedAt,
	}
}

func (u *User) DBCreate() *storage.CreateUserParams {
	return &storage.CreateUserParams{
		ID:           u.ID,
		Username:     u.Username,
		Email:        u.Email,
		PasswordHash: u.PasswordHash,
		Role:         u.Role.ToDB(),
	}
}

func UserFromDB(row *storage.DivaUser) *User {
	return &User{
		ID:           row.ID,
		Username:     row.Username,
		Email:        row.Email,
		PhoneNumber:  row.PhoneNumber,
		PasswordHash: row.PasswordHash,
		Role:         RoleFromDB(row.Role),
		CreatedAt:    row.CreatedAt,
		UpdatedAt:    row.UpdatedAt,
		DeletedAt:    row.DeletedAt,
	}
}
