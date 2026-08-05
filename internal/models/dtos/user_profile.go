package dtos

type CreateProfileDto struct {
	FirstName string `json:"first_name" validate:"required,max=255"`
	LastName  string `json:"last_name" validate:"required,max=255"`
	Alias     string `json:"alias" validate:"required,max=255"`
	Bio       string `json:"bio" validate:"omitempty,max=255"`
	BirthDate int64  `json:"birth_date" validate:"required,gt=0"`
}

type UpdateProfileDto struct {
	FirstName string `json:"first_name" validate:"required,max=255"`
	LastName  string `json:"last_name" validate:"required,max=255"`
	Alias     string `json:"alias" validate:"required,max=255"`
	Bio       string `json:"bio" validate:"omitempty,max=255"`
	BirthDate int64  `json:"birth_date" validate:"required,gt=0"`
}
