package com.diva.app.library.presentation.state

import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaType
import com.diva.app.media.tag.models.Tag
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User

private val OWNER = User(id = "user-1", username = "juevigrace")

private fun media(
    id: String,
    title: String,
    durationMs: Long,
    mediaType: MediaType = MediaType.AUDIO,
    tags: List<String> = emptyList(),
): Media = Media(
    id = id,
    submittedBy = OWNER,
    mediaType = mediaType,
    title = title,
    uri = "/media/$id",
    mimeType = if (mediaType == MediaType.VIDEO) "video/mp4" else "audio/mpeg",
    sizeBytes = 6_200_000L,
    durationMs = Option.of(durationMs),
    tags = tags.mapIndexed { index, name -> Tag(id = "tag-$id-$index", name = name) },
)

private val CATALOG = listOf(
    media("m-01", "Midnight Drive", 254_000L, tags = listOf("favourite", "night")),
    media("m-02", "Neon Coastline", 198_000L, tags = listOf("favourite")),
    media("m-03", "Paper Lanterns", 312_000L),
    media("m-04", "Static Bloom", 227_000L, tags = listOf("chill")),
    media("m-05", "Long Way Home", 285_000L),
    media("m-06", "Harbour Lights", 241_000L, tags = listOf("favourite", "live")),
    media("m-07", "Slow Tide", 203_000L),
    media("m-08", "Amber Static", 276_000L, tags = listOf("chill")),
    media("m-09", "Coastal Fog", 219_000L),
    media("m-10", "Night Ferry", 298_000L, tags = listOf("favourite")),
    media(
        id = "v-01",
        title = "Studio Session Reel",
        durationMs = 512_000L,
        mediaType = MediaType.VIDEO,
        tags = listOf("favourite", "live"),
    ),
    media(
        id = "v-02",
        title = "Rooftop Take",
        durationMs = 341_000L,
        mediaType = MediaType.VIDEO,
    ),
)

private val FOLDERS = listOf(
    Folder(id = "f-01", user = OWNER, name = "Albums", path = "/music/albums"),
    Folder(id = "f-02", user = OWNER, name = "Live Sets", path = "/music/live"),
    Folder(id = "f-03", user = OWNER, name = "Downloads", path = "/music/downloads"),
    Folder(id = "f-04", user = OWNER, name = "Videos", path = "/media/videos"),
)

private val DEFAULT_FAVORITES = CATALOG
    .filter { item -> item.tags.any { tag -> tag.name == "favourite" } }
    .mapTo(mutableSetOf()) { it.id }

internal fun defaultFavoriteIds(): Set<String> = DEFAULT_FAVORITES

internal fun librarySections(
    filter: LibraryFilter,
    favorites: Set<String>,
): List<LibrarySection> {
    val liked = CATALOG.filter { it.id in favorites }
    return when (filter) {
        LibraryFilter.ALL -> listOf(
            LibrarySection("continue", "Continue listening", CATALOG.take(6)),
            LibrarySection("recent", "Recently added", CATALOG.drop(4).take(6)),
            LibrarySection("videos", "Videos", CATALOG.filter { it.mediaType == MediaType.VIDEO }),
        )
        LibraryFilter.FAVORITES -> listOf(LibrarySection("fav", "Your favourites", liked))
        LibraryFilter.RECENT -> listOf(LibrarySection("recent", "Recently played", CATALOG.reversed().take(8)))
        LibraryFilter.RESUMABLE -> listOf(LibrarySection("resume", "Pick up where you left off", CATALOG.take(4)))
        LibraryFilter.FOLDERS -> emptyList()
    }.filter { it.media.isNotEmpty() }
}

internal fun libraryFolders(): List<Folder> = FOLDERS