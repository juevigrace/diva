package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type UserProfile struct {
	UserID    uuid.UUID
	FirstName string
	LastName  string
	BirthDate int64
	Alias     string
	Avatar    string
	Bio       string
	UpdatedAt int64
}

func (up *UserProfile) Response() *responses.UserProfileResponse {
	return &responses.UserProfileResponse{
		FirstName: up.FirstName,
		LastName:  up.LastName,
		BirthDate: up.BirthDate,
		Alias:     up.Alias,
		Avatar:    up.Avatar,
		Bio:       up.Bio,
	}
}

func (up *UserProfile) DBCreate(userID uuid.UUID) *storage.CreateUserProfileParams {
	return &storage.CreateUserProfileParams{
		UserID:    userID,
		FirstName: up.FirstName,
		LastName:  up.LastName,
		BirthDate: &up.BirthDate,
		Alias:     up.Alias,
		Bio:       up.Bio,
	}
}

func (up *UserProfile) DBUpdate(userID uuid.UUID) *storage.UpdateUserProfileParams {
	return &storage.UpdateUserProfileParams{
		UserID:    userID,
		FirstName: up.FirstName,
		LastName:  up.LastName,
		BirthDate: &up.BirthDate,
		Alias:     up.Alias,
		Bio:       up.Bio,
	}
}

func UserProfileFromDB(row *storage.DivaUserProfile) *UserProfile {
	birthDate := int64(0)
	if row.BirthDate != nil {
		birthDate = *row.BirthDate
	}
	return &UserProfile{
		UserID:    row.UserID,
		FirstName: row.FirstName,
		LastName:  row.LastName,
		BirthDate: birthDate,
		Alias:     row.Alias,
		Bio:       row.Bio,
		Avatar:    row.Avatar,
		UpdatedAt: row.UpdatedAt,
	}
}
