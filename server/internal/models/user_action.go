package models

import (
	"time"

	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/models/responses"
	"github.com/juevigrace/diva-server/storage"
)

type UserAction struct {
	ID     uuid.UUID
	Name   Action
	UserID uuid.UUID
}

type UserActionVerification struct {
	Action    UserAction
	Token     string
	ExpiresAt time.Time
	UsedAt    *time.Time
	Verified  bool
}

func (ua *UserAction) Response() *responses.UserActionResponse {
	return &responses.UserActionResponse{
		ID:         ua.ID.String(),
		ActionName: ua.Name.String(),
	}
}

func (ua *UserAction) DBCreate() *storage.CreateUserActionParams {
	return &storage.CreateUserActionParams{
		ID:     ua.ID,
		Name:   ua.Name.String(),
		UserID: ua.UserID,
	}
}

func (uv *UserActionVerification) DBCreate() *storage.CreateUserVerificationParams {
	return &storage.CreateUserVerificationParams{
		ActionID:  uv.Action.ID,
		Token:     uv.Token,
		ExpiresAt: uv.ExpiresAt.UnixMilli()}
}

func UserActionFromDB(row *storage.DivaAction) *UserAction {
	return &UserAction{
		ID:     row.ID,
		Name:   ActionFromString(row.Name),
		UserID: row.UserID,
	}
}
