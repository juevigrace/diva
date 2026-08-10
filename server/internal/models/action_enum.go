package models

type Action int

const (
	ActionUserVerification Action = iota
	ActionPasswordUpdate
	ActionEmailUpdate
	ActionUsernameUpdate
	ActionPhoneUpdate
	ActionUserRestore
)

func (a Action) String() string {
	switch a {
	case ActionUserVerification:
		return "USER_VERIFICATION"
	case ActionPasswordUpdate:
		return "PASSWORD_RESET"
	case ActionEmailUpdate:
		return "EMAIL_UPDATE"
	case ActionUsernameUpdate:
		return "USERNAME_UPDATE"
	case ActionPhoneUpdate:
		return "PHONE_UPDATE"
	case ActionUserRestore:
		return "USER_RESTORE"
	default:
		return ""
	}
}

func ActionFromString(s string) Action {
	switch s {
	case "USER_VERIFICATION":
		return ActionUserVerification
	case "PASSWORD_RESET":
		return ActionPasswordUpdate
	case "EMAIL_UPDATE":
		return ActionEmailUpdate
	case "USERNAME_UPDATE":
		return ActionUsernameUpdate
	case "PHONE_UPDATE":
		return ActionPhoneUpdate
	case "USER_RESTORE":
		return ActionUserRestore
	default:
		return -1
	}
}
