@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.playback

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class PlaybackHistory(
    val id: String,
    val userId: String,
    val mediaId: String,
    val playedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val positionMs: Long = 0,
    val completed: Boolean = false,
)
