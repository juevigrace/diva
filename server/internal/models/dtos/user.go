package dtos

type CreateUserDto struct {
	Email    string `json:"email" validate:"required,email,max=100"`
	Username string `json:"username" validate:"required,min=3,max=50"`
	Password string `json:"password" validate:"required,min=4,max=255"`
}

type UpdateUsernameDto struct {
	Username string `json:"username" validate:"required,min=3,max=50"`
}

type UpdatePasswordDto struct {
	NewPassword string `json:"new_password" validate:"required,min=4,max=255"`
}

type UpdatePhoneNumberDto struct {
	PhoneNumber string `json:"phone_number" validate:"required,max=30"`
}

type UpdateEmailDto struct {
	Email string `json:"email" validate:"required,email,max=100"`
}

type UpdateRole struct {
	Role string `json:"role" validate:"required,oneof=USER MODERATOR ADMIN"`
}
