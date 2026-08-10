package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type UserPreferences struct {
	ID                  uuid.UUID
	UserID              uuid.UUID
	Theme               Theme
	OnboardingCompleted bool
	Language            string
	LastSyncAt          int64
	CreatedAt           int64
	UpdatedAt           int64
}

func (up *UserPreferences) Response() *responses.UserPreferencesResponse {
	return &responses.UserPreferencesResponse{
		Id:                  up.ID.String(),
		Theme:               up.Theme.String(),
		OnboardingCompleted: up.OnboardingCompleted,
		Language:            up.Language,
		LastSyncAt:          up.LastSyncAt,
		CreatedAt:           up.CreatedAt,
		UpdatedAt:           up.UpdatedAt,
	}
}

func (up *UserPreferences) DBCreate(userID uuid.UUID) *storage.CreateUserPreferencesParams {
	return &storage.CreateUserPreferencesParams{
		ID:                  up.ID,
		UserID:              userID,
		Theme:               up.Theme.ToDB(),
		OnboardingCompleted: up.OnboardingCompleted,
		Language:            up.Language,
	}
}

func (up *UserPreferences) DBUpdate() *storage.UpdateUserPreferencesParams {
	return &storage.UpdateUserPreferencesParams{
		ID:       up.ID,
		Theme:    up.Theme.ToDB(),
		Language: up.Language,
	}
}

func UserPrefsFromDB(row *storage.DivaUserPreference) *UserPreferences {
	return &UserPreferences{
		ID:                  row.ID,
		UserID:              row.UserID,
		Theme:               ThemeFromDB(row.Theme),
		OnboardingCompleted: row.OnboardingCompleted,
		Language:            row.Language,
		LastSyncAt:          row.LastSyncAt,
		CreatedAt:           row.CreatedAt,
		UpdatedAt:           row.UpdatedAt,
	}
}
