@file:DivaJsExport

package com.diva.app.player.playback.models

import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.time.Clock

data class PlaybackHistory(
    val id: String,
    val user: User,
    val media: Media,
    val playedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val positionMs: Long = 0,
    val completed: Boolean = false,
)
