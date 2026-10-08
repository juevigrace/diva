package com.diva.app.home.presentation.state

import com.diva.app.home.presentation.ui.models.HomeSection
import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaType
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User

private val OWNER = User(id = "user-1", username = "juevigrace")

private fun media(
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
    mimeType = if (mediaType == MediaType.VIDEO) "video/mp4" else "audio/mpeg",
    sizeBytes = 6_200_000L,
    durationMs = Option.of(durationMs),
)

private val CATALOG = listOf(
    media("h-01", "Midnight Drive", 254_000L),
    media("h-02", "Neon Coastline", 198_000L),
    media("h-03", "Paper Lanterns", 312_000L),
    media("h-04", "Static Bloom", 227_000L),
    media("h-05", "Long Way Home", 285_000L),
    media("h-06", "Harbour Lights", 241_000L),
    media("h-07", "Slow Tide", 203_000L),
    media("h-08", "Amber Static", 276_000L),
    media("v-01", "Studio Session Reel", 512_000L, MediaType.VIDEO),
    media("v-02", "Rooftop Take", 341_000L, MediaType.VIDEO),
)

// TODO: make these actual sections and fetch collections based on each
internal fun mockHomeState(): HomeState = HomeState(
    sections = listOf(
        HomeSection("made-for-you", "Made for you", CATALOG.take(6)),
        HomeSection("recent", "Pick up where you left off", CATALOG.drop(3).take(6)),
        HomeSection(
            "videos",
            "Watch something",
            CATALOG.filter { it.mediaType == MediaType.VIDEO }),
    ),
)