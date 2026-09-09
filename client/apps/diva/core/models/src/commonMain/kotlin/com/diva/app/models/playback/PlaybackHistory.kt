package com.diva.app.models.playback

import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class PlaybackHistory(
    val id: Uuid,
    val userId: Uuid,
    val mediaId: Uuid,
    val playedAt: Instant = Clock.System.now(),
    val positionMs: Long = 0,
    val completed: Boolean = false,
)
