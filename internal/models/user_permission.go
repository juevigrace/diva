package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type UserPermission struct {
	Permission Permission
	UserID     uuid.UUID
	GrantedBy  *uuid.UUID
	Granted    bool
	GrantedAt  int64
	// TODO: change expiration time for enum with fixed times
	ExpiresAt *int64
	UpdatedAt int64
}

func (up *UserPermission) Response() *responses.UserPermissionResponse {
	var grantedBy *string
	if up.GrantedBy != nil {
		grantedBy = new(string)
		*grantedBy = up.GrantedBy.String()
	}

	return &responses.UserPermissionResponse{
		PermissionID: up.Permission.ID.String(),
		GrantedBy:    grantedBy,
		Granted:      up.Granted,
		GrantedAt:    up.GrantedAt,
		ExpiresAt:    up.ExpiresAt,
		UpdatedAt:    up.UpdatedAt,
	}
}

func (up *UserPermission) DBCreate(userID uuid.UUID) *storage.CreateUserPermissionParams {
	return &storage.CreateUserPermissionParams{
		PermissionID: up.Permission.ID,
		UserID:       userID,
		GrantedBy:    up.GrantedBy,
		Granted:      up.Granted,
		ExpiresAt:    up.ExpiresAt,
	}
}

func (up *UserPermission) DBUpdate(userID uuid.UUID) *storage.UpdateUserPermissionParams {
	return &storage.UpdateUserPermissionParams{
		PermissionID: up.Permission.ID,
		UserID:       userID,
		Granted:      up.Granted,
		ExpiresAt:    up.ExpiresAt,
	}
}

func UserPermissionFromDB(row *storage.DivaUserPermission, perm *Permission) *UserPermission {
	return &UserPermission{
		Permission: *perm,
		UserID:     row.UserID,
		GrantedBy:  row.GrantedBy,
		Granted:    row.Granted,
		GrantedAt:  row.GrantedAt,
		ExpiresAt:  row.ExpiresAt,
		UpdatedAt:  row.UpdatedAt,
	}
}
