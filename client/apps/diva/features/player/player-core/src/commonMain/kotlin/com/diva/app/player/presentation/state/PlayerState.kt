package com.diva.app.player.presentation.state

import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaMetadata
import com.diva.app.player.models.PlayerSetting
import com.diva.app.player.models.RepeatMode
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.user.models.User

data class PlayerState(
    val title: String = "Player",
    val media: Media = Media(id = "", title = "", uri = ""),
    val metadata: MediaMetadata = MediaMetadata(mediaId = ""),
    val settings: PlayerSetting = PlayerSetting(user = User(id = "user-1")),
    val queue: List<Media> = emptyList(),
    val positionMs: Long = 0,
    val isPlaying: Boolean = true,
    val isBuffering: Boolean = false,
    val showLyrics: Boolean = false,
) {
    val durationMs: Long
        get() = media.durationMs.getOrNull() ?: 0L

    val progress: Float
        get() = if (durationMs <= 0L) 0f else (positionMs.toFloat() / durationMs).coerceIn(0f, 1f)

    val repeatMode: RepeatMode
        get() = settings.repeatMode

    val shuffle: Boolean
        get() = settings.shuffle
}