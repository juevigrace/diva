package responses

type UserProfileResponse struct {
	FirstName   string `json:"first_name"`
	LastName    string `json:"last_name"`
	BirthDate   int64  `json:"birth_date"`
	PhoneNumber string `json:"phone_number"`
	Alias       string `json:"alias"`
	Avatar      string `json:"avatar"`
	Bio         string `json:"bio"`
}
