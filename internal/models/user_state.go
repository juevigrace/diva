package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type UserState struct {
	Verified     bool
	Status       UserStatus
	LastActiveAt int64
	UpdatedAt    int64
}

func (us *UserState) Response() *responses.UserStateResponse {
	return &responses.UserStateResponse{
		Verified:     us.Verified,
		Status:       us.Status.String(),
		LastActiveAt: us.LastActiveAt,
		UpdatedAt:    us.UpdatedAt,
	}
}

func (us *UserState) DBCreate(userID uuid.UUID) *storage.CreateUserStateParams {
	return &storage.CreateUserStateParams{
		UserID:   userID,
		Verified: us.Verified,
		Status:   us.Status.ToDB(),
	}
}

func UserStateFromDB(row *storage.DivaUserState) *UserState {
	return &UserState{
		Verified:     row.Verified,
		Status:       UserStatusFromDB(row.Status),
		LastActiveAt: row.LastActiveAt,
		UpdatedAt:    row.UpdatedAt,
	}
}
