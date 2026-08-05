package models

import (
	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type Permission struct {
	ID          uuid.UUID
	Name        string
	Description string
	Action      PermissionAction
	RoleLevel   Role
	CreatedAt   int64
	UpdatedAt   int64
	DeletedAt   *int64
}

func (p *Permission) Response() *responses.PermissionResponse {
	return &responses.PermissionResponse{
		ID:          p.ID.String(),
		Name:        p.Name,
		Description: p.Description,
		Action:      p.Action.String(),
		RoleLevel:   p.RoleLevel.String(),
		CreatedAt:   p.CreatedAt,
		UpdatedAt:   p.UpdatedAt,
	}
}

func (p *Permission) DBUpdate() *storage.UpdatePermissionParams {
	return &storage.UpdatePermissionParams{
		ID:          p.ID,
		Name:        p.Name,
		Description: p.Description,
	}
}

func PermissionFromDB(row *storage.DivaPermission) *Permission {
	return &Permission{
		ID:          row.ID,
		Name:        row.Name,
		Description: row.Description,
		Action:      PermissionActionFromString(row.Action),
		RoleLevel:   RoleFromDB(row.RoleLevel),
		CreatedAt:   row.CreatedAt,
		UpdatedAt:   row.UpdatedAt,
		DeletedAt:   row.DeletedAt,
	}
}
