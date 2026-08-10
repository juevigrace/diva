# AGENTS.md

## Build & verify — server

Always use the Makefile targets. Do NOT run `go build`, `go test`, or other go toolchain commands directly.

- `make build` — compile the server binary
- `make test` — run unit tests (`go test ./...`)
- `make itest` — run DB integration tests (requires dev DB)
- `make sqlc` — regenerate sqlc code after SQL changes
- `make templ-build` — regenerate templ code
- `make watch` — live reload

After editing Go code, always verify with `make build` (and `make test` when tests are affected). Note: `make test` includes `./internal/database` integration tests which require a live DB.
