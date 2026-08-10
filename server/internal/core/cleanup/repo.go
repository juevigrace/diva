package cleanup

import (
	"context"
	"log"
	"time"

	"github.com/juevigrace/diva-server/internal/core/session"
	"github.com/juevigrace/diva-server/internal/core/user/actions"
	"github.com/juevigrace/diva-server/internal/core/user/permissions"
)

type CleanupService struct {
	sessionRepo  *session.SessionRepo
	permRepo     *permissions.UserPermissionRepo
	actionRepo   *actions.UserActionsRepo
	interval     time.Duration
	stopCh       chan struct{}
}

func NewCleanupService(
	sessionRepo *session.SessionRepo,
	permRepo *permissions.UserPermissionRepo,
	actionRepo *actions.UserActionsRepo,
) *CleanupService {
	return &CleanupService{
		sessionRepo: sessionRepo,
		permRepo:    permRepo,
		actionRepo:  actionRepo,
		interval:    15 * time.Minute,
		stopCh:      make(chan struct{}),
	}
}

func (s *CleanupService) Start() {
	go func() {
		s.run()

		ticker := time.NewTicker(s.interval)
		defer ticker.Stop()

		for {
			select {
			case <-ticker.C:
				s.run()
			case <-s.stopCh:
				log.Println("cleanup service stopped")
				return
			}
		}
	}()
	log.Println("cleanup service started")
}

func (s *CleanupService) Stop() {
	close(s.stopCh)
}

func (s *CleanupService) run() {
	ctx := context.Background()

	if err := s.sessionRepo.CloseExpired(ctx); err != nil {
		log.Printf("cleanup: close expired sessions: %v", err)
	}

	if err := s.permRepo.DeleteExpired(ctx); err != nil {
		log.Printf("cleanup: delete expired user permissions: %v", err)
	}

	if err := s.actionRepo.DeleteExpired(ctx); err != nil {
		log.Printf("cleanup: delete expired actions: %v", err)
	}
}
