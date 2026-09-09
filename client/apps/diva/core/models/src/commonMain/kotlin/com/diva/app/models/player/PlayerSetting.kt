package com.diva.app.models.player

import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class PlayerSetting(
    val userId: Uuid,
    val volume: Float = 1.0f,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.NONE,
    val shuffle: Boolean = false,
    val updatedAt: Instant = Clock.System.now(),
)
