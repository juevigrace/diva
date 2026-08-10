package dtos

type CreateUserPreferencesDto struct {
	Theme               string `json:"theme" validate:"required,oneof=LIGHT DARK SYSTEM"`
	OnboardingCompleted bool   `json:"onboarding_completed" validate:"required"`
	Language            string `json:"language" validate:"required,max=10"`
}

type UpdateUserPreferencesDto struct {
	Theme    string `json:"theme" validate:"required,oneof=LIGHT DARK SYSTEM"`
	Language string `json:"language" validate:"required,max=10"`
}
