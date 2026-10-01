package com.diva.app.player.presentation.state

import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaMetadata
import com.diva.app.media.models.MediaType
import com.diva.app.media.tag.models.Tag
import com.diva.app.player.models.PlayerSetting
import com.diva.app.player.models.RepeatMode
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User

private val OWNER = User(id = "user-1", username = "juevigrace")

internal fun mockMedia(
    id: String,
    title: String,
    durationMs: Long,
    mediaType: MediaType = MediaType.AUDIO,
): Media = Media(
    id = id,
    submittedBy = OWNER,
    mediaType = mediaType,
    title = title,
    uri = "/media/$id",
    mimeType = "audio/mpeg",
    sizeBytes = 8_400_000L,
    durationMs = Option.of(durationMs),
    width = 0,
    height = 0,
    tags = listOf(
        Tag(id = "tag-1", name = "favourite"),
        Tag(id = "tag-2", name = "chill"),
    ),
)

internal fun mockPlayerState(): PlayerState {
    val nowPlaying = mockMedia("media-1", "Midnight Drive", 254_000L)
    val queue = listOf(
        mockMedia("media-1", "Midnight Drive", 254_000L),
        mockMedia("media-2", "Neon Coastline", 198_000L),
        mockMedia("media-3", "Paper Lanterns", 312_000L),
        mockMedia("media-4", "Static Bloom", 227_000L),
        mockMedia("media-5", "Long Way Home", 285_000L),
    )

    return PlayerState(
        title = "Player",
        media = nowPlaying,
        metadata = MediaMetadata(
            mediaId = nowPlaying.id,
            album = "After Hours",
            artist = "Kaya Lin",
            genre = "Electronic",
            year = Option.of(2024),
            trackNumber = Option.of(3),
            discNumber = Option.of(1),
            coverUri = "",
            lyrics = "Neon on the windshield, city folding into two\n" +
                "Every light a heartbeat I keep running into you\n" +
                "Midnight drive, nothing left to prove",
        ),
        settings = PlayerSetting(
            user = OWNER,
            volume = 0.72f,
            playbackSpeed = 1f,
            repeatMode = RepeatMode.ALL,
            shuffle = false,
        ),
        queue = queue,
        positionMs = 96_000L,
        isPlaying = true,
        isBuffering = false,
        showLyrics = false,
    )
}