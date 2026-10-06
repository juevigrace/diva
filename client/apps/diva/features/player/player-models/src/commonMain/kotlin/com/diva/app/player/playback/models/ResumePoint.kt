@file:DivaJsExport

package com.diva.app.player.playback.models

import com.diva.app.media.models.Media
import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.user.models.User
import kotlin.time.Clock

data class ResumePoint(
    val user: User,
    val media: Media,
    val positionMs: Long = 0,
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)