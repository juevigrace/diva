package io.github.juevigrace.diva.lib.models.session

import io.github.juevigrace.diva.lib.models.api.auth.session.SessionDataDto
import io.github.juevigrace.diva.lib.models.api.auth.session.SessionResponse

fun SessionData.toSessionDataDto(): SessionDataDto {
    return SessionDataDto(
        device = device,
        userAgent = agent,
    )
}

fun SessionDataDto.toSessionData(): SessionData {
    return SessionData(
        device = device,
        agent = userAgent.orEmpty(),
        ip = "",
    )
}

fun SessionResponse.toSession(): Session {
    return Session(
        id = sessionId,
        userId = userId,
        accessToken = accessToken,
        refreshToken = refreshToken,
        type = safeSessionType(type),
        status = safeSessionStatus(status),
        data = SessionData(
            device = deviceId,
            agent = agent,
            ip = ip,
        ),
        accessExpiresAt = accessExpiresAt,
        refreshExpiresAt = refreshExpiresAt,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}