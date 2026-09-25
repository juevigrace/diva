@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.session

import io.github.juevigrace.diva.core.DivaJsExport
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
)