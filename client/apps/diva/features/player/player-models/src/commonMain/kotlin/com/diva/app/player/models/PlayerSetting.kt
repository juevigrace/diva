@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.player.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.js.ExperimentalJsExport
import kotlin.time.Clock

data class PlayerSetting(
    val user: User,
    val volume: Float = 1.0f,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val shuffle: Boolean = false,
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)
