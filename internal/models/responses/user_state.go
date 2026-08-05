package responses

type UserStateResponse struct {
	Verified     bool   `json:"verified"`
	Status       string `json:"status"`
	LastActiveAt int64  `json:"last_active_at"`
	UpdatedAt    int64  `json:"updated_at"`
}
