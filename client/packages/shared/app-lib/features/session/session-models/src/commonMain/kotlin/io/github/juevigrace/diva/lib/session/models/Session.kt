@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.session.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.session.models.api.SessionDataDto
import io.github.juevigrace.diva.lib.session.models.api.SessionResponse
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class Session(
    val id: String,
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val type: SessionType = SessionType.NORMAL,
    val status: SessionStatus,
    val isCurrent: Boolean = false,
    val data: SessionData,
    val accessExpiresAt: Long,
    val refreshExpiresAt: Long,
    val expired: Boolean = accessExpiresAt < Clock.System.now().toEpochMilliseconds(),
    val createdAt: Long,
    val updatedAt: Long,
) {
    companion object {
        fun fromResponse(response: SessionResponse): Session {
            return Session(
                id = response.sessionId,
                userId = response.userId,
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                type = safeSessionType(response.type),
                status = safeSessionStatus(response.status),
                data = SessionData(
                    device = response.deviceId,
                    agent = response.agent,
                    ip = response.ip,
                ),
                accessExpiresAt = response.accessExpiresAt,
                refreshExpiresAt = response.refreshExpiresAt,
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
            )
        }
    }
}

data class SessionData(
    val device: String = "",
    val agent: String = "",
    val ip: String = "",
) {
    fun toDto(): SessionDataDto {
        return SessionDataDto(
            device = device,
            userAgent = agent,
        )
    }
}
