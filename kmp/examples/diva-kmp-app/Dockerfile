# Stage 1: build
FROM gradle:9.3.1-jdk21-alpine AS build
WORKDIR /app
ENV GRADLE_USER_HOME=/home/gradle/.gradle
COPY gradle gradle
COPY build.gradle.kts gradle.properties settings.gradle.kts ./
COPY build-logic build-logic

# Core server modules
COPY server server
COPY core/server core/server
COPY core/models-server core/models-server
COPY core/models core/models
COPY core/database/database-server core/database/database-server
COPY core/database/database-shared core/database/database-shared

# Auth modules
COPY features/auth/auth-server features/auth/auth-server

# Chat modules
COPY features/chat/chat-server features/chat/chat-server

# Collection modules
COPY features/collection/collection-server features/collection/collection-server

# Media modules
COPY features/media/media-server features/media/media-server

# Permissions modules
COPY features/permissions/permissions-server features/permissions/permissions-server

# Session modules
COPY features/session/session-server features/session/session-server

# Social modules
COPY features/social/social-server features/social/social-server

# User modules
COPY features/user/user-server features/user/user-server

# Verification modules
COPY features/verification features/verification

RUN gradle :server:buildFatJar -PexcludeCompose=true --no-daemon

# Stage 2: Create the Runtime Image
FROM amazoncorretto:21-alpine AS runtime
WORKDIR /app
RUN addgroup -g 1000 appgroup && adduser -u 1000 -G appgroup -s /bin/sh -D diva
RUN mkdir -p ./bin
COPY --from=build /app/server/build/libs/diva-server.jar ./bin/diva-server.jar
USER diva
