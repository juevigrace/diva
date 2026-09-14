@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.player

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class PlayerSetting(
    val userId: String,
    val volume: Float = 1.0f,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val shuffle: Boolean = false,
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
