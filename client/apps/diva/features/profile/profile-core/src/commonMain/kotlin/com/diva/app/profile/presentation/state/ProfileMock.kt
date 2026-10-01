package com.diva.app.profile.presentation.state

import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaType
import com.diva.app.profile.models.Profile
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User

private val OWNER = User(id = "user-1", username = "juevigrace")

// TODO(ui-pass): replace with repository-backed profile
internal fun mockProfile(): Profile = Profile(
    username = "juevigrace",
    email = Option.of("juevigrace@diva.local"),
    phoneNumber = Option.of("+63 900 000 0000"),
    alias = "night owl",
    bio = "Collecting records and shooting film since forever.",
    devices = listOf("Pixel 9", "Studio Mac", "Living Room TV"),
)

private fun media(id: String, title: String, durationMs: Long): Media = Media(
    id = id,
    submittedBy = OWNER,
    mediaType = MediaType.AUDIO,
    title = title,
    uri = "/media/$id",
    mimeType = "audio/mpeg",
    sizeBytes = 6_200_000L,
    durationMs = Option.of(durationMs),
)

private val RECENT = listOf(
    media("p-01", "Midnight Drive", 254_000L),
    media("p-02", "Neon Coastline", 198_000L),
    media("p-03", "Paper Lanterns", 312_000L),
    media("p-04", "Static Bloom", 227_000L),
    media("p-05", "Long Way Home", 285_000L),
)

internal fun mockStats(): List<ProfileQuickStat> = listOf(
    ProfileQuickStat("Albums", "42"),
    ProfileQuickStat("Playlists", "8"),
    ProfileQuickStat("Favourites", "17"),
    ProfileQuickStat("Hours", "128"),
)

internal fun mockRecentMedia(): List<Media> = RECENT