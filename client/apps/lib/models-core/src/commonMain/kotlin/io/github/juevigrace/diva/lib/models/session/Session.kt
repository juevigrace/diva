package io.github.juevigrace.diva.lib.models.session

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionResponse
import io.github.juevigrace.diva.lib.models.user.User
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Session(
    val id: Uuid,
    val user: User,
    val accessToken: String,
    val refreshToken: String,
    val type: SessionType = SessionType.NORMAL,
    val status: SessionStatus,
    val isCurrent: Boolean = false,
    val data: SessionData,
    val accessExpiresAt: Instant,
    val refreshExpiresAt: Instant,
    val expired: Boolean = accessExpiresAt < Clock.System.now(),
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromResponse(response: SessionResponse): Session {
            return Session(
                id = Uuid.parse(response.sessionId),
                user = User(id = Uuid.parse(response.userId)),
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                type = safeSessionType(response.type),
                status = safeSessionStatus(response.status),
                data = SessionData(
                    device = response.deviceId,
                    agent = response.agent,
                    ip = response.ip,
                ),
                accessExpiresAt = Instant.fromEpochMilliseconds(response.accessExpiresAt),
                refreshExpiresAt = Instant.fromEpochMilliseconds(response.refreshExpiresAt),
                createdAt = Instant.fromEpochMilliseconds(response.createdAt),
                updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
            )
        }
    }
}
