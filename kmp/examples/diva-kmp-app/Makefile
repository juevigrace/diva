.PHONY: help dev-build dev-up dev-down dev-logs dev-shell prod-build prod-up prod-down prod-logs clean ps db-shell rebuild

# Default target
help:
	@echo "Available commands:"
	@echo ""
	@echo "Development:"
	@echo "  dev-build    Build development image"
	@echo "  dev-up       Start development environment"
	@echo "  dev-down     Stop development environment"
	@echo "  dev-logs     View development logs"
	@echo "  dev-shell    Access development container shell"
	@echo ""
	@echo "Production:"
	@echo "  prod-build   Build production image"
	@echo "  prod-up      Start production environment"
	@echo "  prod-down    Stop production environment"
	@echo "  prod-logs    View production logs"
	@echo ""
	@echo "Utilities:"
	@echo "  clean        Clean up containers and images"
	@echo "  ps           Show running containers"
	@echo "  db-shell     Access database shell"
	@echo "  rebuild      Force rebuild without cache"
	@echo ""

# Development Commands
dev-up:
	@echo "Starting development environment..."
	docker compose -f docker-compose.dev.yml --env-file .env.dev up -d --build

dev-down:
	@echo "Stopping development environment..."
	docker-compose -f docker-compose.dev.yml down

dev-logs:
	@echo "Following development logs..."
	docker-compose -f docker-compose.dev.yml logs -f

dev-shell:
	@echo "Accessing development container shell..."
	docker-compose -f docker-compose.dev.yml exec diva_ktor_server sh

# Production Commands
prod-up:
	@echo "Starting production environment..."
	docker compose -f docker-compose.yml --env-file .env up -d --build

prod-down:
	@echo "Stopping production environment..."
	docker-compose down

prod-logs:
	@echo "Following production logs..."
	docker-compose logs -f

# Utility Commands
clean:
	@echo "Cleaning up containers and images..."
	docker-compose -f docker-compose.dev.yml down -v --remove-orphans
	docker-compose down -v --remove-orphans
	docker system prune -f

ps:
	@echo "Running containers:"
	docker ps -a

db-shell:
	@echo "Accessing database shell..."
	docker-compose -f docker-compose.dev.yml exec diva_db psql -U ${DB_USER} -d ${DB_NAME}

rebuild:
	@echo "Force rebuilding without cache..."
	docker-compose -f docker-compose.dev.yml build --no-cache
	docker-compose build --no-cache

# Quick start commands
dev-start: dev-build dev-up
	@echo "Development environment started!"

prod-start: prod-build prod-up
	@echo "Production environment started!"

dev-stop: dev-down
	@echo "Development environment stopped!"

prod-stop: prod-down
	@echo "Production environment stopped!"

# Full reset
reset: clean
	@echo "Environment reset complete!"