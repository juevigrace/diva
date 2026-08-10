package dtos

type CreateUserPermissionDto struct {
	PermissionAction string `json:"permission_action" validate:"required,max=255"`
	Granted          bool   `json:"granted" validate:"required"`
	ExpiresAt        *int64 `json:"expires_at"`
}

type UpdateUserPermissionDto struct {
	Granted   bool   `json:"granted" validate:"required"`
	ExpiresAt *int64 `json:"expires_at"`
}
