package auth

import (
	"context"

	"github.com/google/uuid"
	"github.com/juevigrace/diva-server/internal/core/device"
	"github.com/juevigrace/diva-server/internal/core/permission"
	"github.com/juevigrace/diva-server/internal/core/session"
	"github.com/juevigrace/diva-server/internal/core/user"
	"github.com/juevigrace/diva-server/internal/core/verification"
	"github.com/juevigrace/diva-server/internal/models"
	"github.com/juevigrace/diva-server/internal/models/dtos"
	"github.com/juevigrace/diva-server/pkg/bcrypt"
	"github.com/juevigrace/diva-server/pkg/errs"
)

type AuthRepo struct {
	pRepo *permission.PermissionRepo
	sRepo *session.SessionRepo
	uRepo *user.UserRepo
	vRepo *verification.VerificationRepo
	dRepo *device.DeviceRepo
}

func NewAuthRepo(
	pRepo *permission.PermissionRepo,
	sRepo *session.SessionRepo,
	uRepo *user.UserRepo,
	vRepo *verification.VerificationRepo,
	dRepo *device.DeviceRepo,
) *AuthRepo {
	return &AuthRepo{
		pRepo: pRepo,
		sRepo: sRepo,
		uRepo: uRepo,
		vRepo: vRepo,
		dRepo: dRepo,
	}
}

func (s *AuthRepo) resolveDevice(ctx context.Context, name string) (*models.Device, error) {
	dev, err := s.dRepo.GetByName(ctx, name)
	if err != nil {
		return s.dRepo.Create(ctx, name)
	}
	return dev, nil
}

func (s *AuthRepo) SignUp(ctx context.Context, dto *dtos.SignUpDto) (*models.Session, error) {
	if ok, err := s.uRepo.CheckUsernameAvailable(ctx, dto.User.Username); err != nil {
		return nil, err
	} else if !ok {
		return nil, errs.ErrUsernameTaken
	}

	if ok, err := s.uRepo.CheckEmailAvailable(ctx, dto.User.Email); err != nil {
		return nil, err
	} else if !ok {
		return nil, errs.ErrEmailTaken
	}

	userID, err := s.uRepo.Create(ctx, &dto.User)
	if err != nil {
		return nil, err
	}

	dev, err := s.resolveDevice(ctx, dto.SessionData.Device)
	if err != nil {
		return nil, err
	}

	if _, err := s.dRepo.GetUserDevice(ctx, userID, dev.ID); err != nil {
		if err := s.dRepo.CreateUserDevice(ctx, userID, dev.ID); err != nil {
			return nil, err
		}
	}

	session, err := s.sRepo.Create(ctx, userID, dev.ID, models.SESSION_NORMAL, dto.SessionData.IpAddress, dto.SessionData.UserAgent)
	if err != nil {
		return nil, err
	}

	return session, nil
}

func (s *AuthRepo) SignIn(ctx context.Context, dto *dtos.SignInDto) (*models.Session, error) {
	user, err := s.uRepo.GetByUsernameOrEmail(ctx, dto.Username)
	if err != nil {
		return nil, errs.ErrInvalidCredentials
	}

	if user.DeletedAt != nil {
		return nil, errs.ErrUserDeleted
	}

	if !bcrypt.ValidatePassword(dto.Password, user.PasswordHash) {
		return nil, errs.ErrInvalidCredentials
	}

	dev, err := s.resolveDevice(ctx, dto.SessionData.Device)
	if err != nil {
		return nil, err
	}

	if _, err := s.dRepo.GetUserDevice(ctx, user.ID, dev.ID); err != nil {
		if err := s.dRepo.CreateUserDevice(ctx, user.ID, dev.ID); err != nil {
			return nil, err
		}
	}

	session, err := s.sRepo.Create(ctx, user.ID, dev.ID, models.SESSION_NORMAL, dto.SessionData.IpAddress, dto.SessionData.UserAgent)
	if err != nil {
		return nil, err
	}

	return session, nil
}

func (s *AuthRepo) Refresh(ctx context.Context, session *models.Session, dto *dtos.SessionDataDto) (*models.Session, error) {
	dev, err := s.dRepo.GetByName(ctx, dto.Device)
	if err != nil || dev.ID != session.Device.ID || session.UserAgent != dto.UserAgent {
		if err := s.sRepo.Close(ctx, session.ID); err != nil {
			return nil, err
		}
		return nil, errs.ErrSessionInvalid
	}
	updated, err := s.sRepo.Update(ctx, session)
	if err != nil {
		return nil, err
	}
	return updated, nil
}

func (s *AuthRepo) ForgotPasswordConfirm(ctx context.Context, actionID uuid.UUID, sd *dtos.SessionDataDto) (*models.Session, error) {
	dbUV, err := s.vRepo.GetByID(ctx, actionID)
	if err != nil {
		return nil, err
	}

	if !dbUV.Verified {
		return nil, errs.ErrActionNotVerified
	}

	dev, err := s.resolveDevice(ctx, sd.Device)
	if err != nil {
		return nil, err
	}

	if _, err := s.dRepo.GetUserDevice(ctx, dbUV.Action.UserID, dev.ID); err != nil {
		if err := s.dRepo.CreateUserDevice(ctx, dbUV.Action.UserID, dev.ID); err != nil {
			return nil, err
		}
	}

	session, err := s.sRepo.CreateTemporal(ctx, dbUV.Action.UserID, dev.ID, sd.IpAddress, sd.UserAgent)
	if err != nil {
		return nil, err
	}

	if err := s.uRepo.UpdatePasswordConfirm(ctx, dbUV.Action.ID, dbUV.Action.UserID); err != nil {
		return nil, err
	}

	return session, nil
}

func (s *AuthRepo) SignOut(ctx context.Context, sessionID uuid.UUID) error {
	return s.sRepo.Close(ctx, sessionID)
}
