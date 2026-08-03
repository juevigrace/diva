package responses

import (
	"errors"
	"net/http"

	"github.com/juevigrace/diva-server/pkg/errs"
)

func HandleReqError(w http.ResponseWriter, err error) {
	switch {
	case errors.Is(err, errs.ErrSessionNotFound),
		errors.Is(err, errs.ErrNotAuthorized),
		errors.Is(err, errs.ErrHeaderNotValid),
		errors.Is(err, errs.ErrInvalidCredentials),
		errors.Is(err, errs.ErrTokenNotValid),
		errors.Is(err, errs.ErrBadAudience),
		errors.Is(err, errs.ErrBadIssuer),
		errors.Is(err, errs.ErrSessionInvalid),
		errors.Is(err, errs.ErrTokenExpired):
		WriteJSON(w, RespondUnauthorized(nil, err.Error()))
	case errors.Is(err, errs.ErrForbidden),
		errors.Is(err, errs.ErrPermissionDenied),
		errors.Is(err, errs.ErrAdminAccessRequired):
		WriteJSON(w, RespondForbidden(nil, err.Error()))
	case errors.Is(err, errs.ErrUserNotFound),
		errors.Is(err, errs.ErrActionNotFound),
		errors.Is(err, errs.ErrPermissionNotFound),
		errors.Is(err, errs.ErrUserStateNotFound),
		errors.Is(err, errs.ErrUserProfileNotFound),
		errors.Is(err, errs.ErrUserPreferencesNotFound),
		errors.Is(err, errs.ErrDeviceNotFound),
		errors.Is(err, errs.ErrUserDeviceNotFound),
		errors.Is(err, errs.ErrUserPermissionNotFound),
		errors.Is(err, errs.ErrVerificationNotFound):
		WriteJSON(w, RespondNotFound(nil, err.Error()))
	case errors.Is(err, errs.ErrUsernameTaken),
		errors.Is(err, errs.ErrEmailTaken),
		errors.Is(err, errs.ErrSamePassword),
		errors.Is(err, errs.ErrUserExists),
		errors.Is(err, errs.ErrUserDeleted),
		errors.Is(err, errs.ErrDeviceNameTaken),
		errors.Is(err, errs.ErrUserDeviceExists),
		errors.Is(err, errs.ErrUserPermissionExists),
		errors.Is(err, errs.ErrUserPreferencesExists),
		errors.Is(err, errs.ErrActionExists):
		WriteJSON(w, RespondConflict(nil, err.Error()))
	default:
		WriteJSON(w, RespondBadRequest(nil, err.Error()))
	}
}
