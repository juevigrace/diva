@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.playback

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ResumePoint(
    val userId: Uuid,
    val mediaId: Uuid,
    val positionMs: Long = 0,
    val updatedAt: Instant = Clock.System.now(),
)