# Production Dockerfile
FROM golang:1.26.1-alpine AS build

# Build dependencies
RUN apk add --no-cache make

ENV CGO_ENABLED=0

WORKDIR /app

# Copy go mod files and download dependencies
COPY go.mod go.sum ./
RUN go mod download && go mod verify

# Copy source code
COPY cmd ./cmd
COPY internal ./internal
COPY server ./server
COPY storage ./storage
COPY pkg ./pkg
COPY Makefile ./

# Build the application
RUN make build-postgres

# Production stage
FROM alpine:3.20.1 AS prod

# Install runtime dependencies
RUN apk --no-cache add curl ca-certificates

WORKDIR /app

# Create non-root user
RUN addgroup -g 1001 -S diva && \
    adduser -u 1001 -S diva -G diva

# Copy binary from build stage
COPY --from=build --chown=diva:diva /app/bin/diva-server /app/bin/diva-server

USER diva

HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:${PORT}/health || exit 1
