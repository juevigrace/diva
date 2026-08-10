package dtos

type UpdateVerified struct {
	Verified bool `json:"verified" validate:"required"`
}

type UpdateUserStatus struct {
	Status string `json:"status" validate:"required,oneof=ACTIVE SUSPENDED INACTIVE"`
}
