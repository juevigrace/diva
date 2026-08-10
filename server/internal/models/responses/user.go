package responses

type UserResponse struct {
	ID          string             `json:"id"`
	Username    string             `json:"username"`
	Email       string             `json:"email"`
	PhoneNumber string             `json:"phone_number"`
	Role        string             `json:"role"`
	State       *UserStateResponse `json:"state"`
	CreatedAt   int64              `json:"created_at"`
	UpdatedAt   int64              `json:"updated_at"`
	DeletedAt   *int64             `json:"deleted_at"`
}
