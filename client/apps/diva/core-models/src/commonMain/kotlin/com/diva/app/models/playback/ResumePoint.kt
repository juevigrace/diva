@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.playback

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class ResumePoint(
    val userId: String,
    val mediaId: String,
    val positionMs: Long = 0,
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)