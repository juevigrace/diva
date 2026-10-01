package com.diva.app.search.presentation.state

import com.diva.app.collection.models.Collection
import com.diva.app.collection.models.CollectionType
import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media
import com.diva.app.media.models.MediaMetadata
import com.diva.app.media.models.MediaType
import com.diva.app.search.models.SearchResults
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.user.models.User

private val OWNER = User(id = "user-1", username = "juevigrace")

private data class Entry(val media: Media, val metadata: MediaMetadata)

private fun entry(
    id: String,
    title: String,
    album: String,
    artist: String,
    genre: String,
    durationMs: Long,
    mediaType: MediaType = MediaType.AUDIO,
): Entry = Entry(
    media = Media(
        id = id,
        submittedBy = OWNER,
        mediaType = mediaType,
        title = title,
        uri = "/media/$id",
        mimeType = if (mediaType == MediaType.VIDEO) "video/mp4" else "audio/mpeg",
        sizeBytes = 6_200_000L,
        durationMs = Option.of(durationMs),
    ),
    metadata = MediaMetadata(
        mediaId = id,
        album = album,
        artist = artist,
        genre = genre,
        year = Option.of(2024),
    ),
)

private val ENTRIES = listOf(
    entry("s-01", "Midnight Drive", "After Hours", "Kaya Lin", "Electronic", 254_000L),
    entry("s-02", "Neon Coastline", "After Hours", "Kaya Lin", "Electronic", 198_000L),
    entry("s-03", "Paper Lanterns", "Soft Focus", "Miro Vale", "Ambient", 312_000L),
    entry("s-04", "Static Bloom", "Soft Focus", "Miro Vale", "Ambient", 227_000L),
    entry("s-05", "Long Way Home", "Field Notes", "Aster Bay", "Folk", 285_000L),
    entry("s-06", "Harbour Lights", "Field Notes", "Aster Bay", "Folk", 241_000L),
    entry("s-07", "Slow Tide", "Low Ceiling", "Nell Okoye", "Jazz", 203_000L),
    entry("s-08", "Amber Static", "Low Ceiling", "Nell Okoye", "Jazz", 276_000L),
    entry("v-01", "Studio Session Reel", "Live Cut", "Aster Bay", "Live", 512_000L, MediaType.VIDEO),
)

private val FOLDERS = listOf(
    Folder(id = "sf-01", user = OWNER, name = "Albums", path = "/music/albums"),
    Folder(id = "sf-02", user = OWNER, name = "Live Sets", path = "/music/live"),
    Folder(id = "sf-03", user = OWNER, name = "Downloads", path = "/music/downloads"),
)

private val COLLECTIONS = listOf(
    Collection(
        id = "sc-01",
        owner = OWNER,
        name = "After Hours",
        description = "Late night listening",
        collectionType = CollectionType.ALBUM,
    ),
    Collection(
        id = "sc-02",
        owner = OWNER,
        name = "Road Trip",
        description = "Long drives, long songs",
        collectionType = CollectionType.PLAYLIST,
    ),
    Collection(
        id = "sc-03",
        owner = OWNER,
        name = "Focus Mix",
        description = "Instrumental deep work",
        collectionType = CollectionType.MIX,
    ),
)

internal fun searchSuggestions(): List<String> = listOf(
    "Kaya Lin",
    "Ambient",
    "Live Sets",
    "Road Trip",
    "Jazz",
)

internal fun searchMock(query: String): SearchResults {
    val term = query.trim()
    if (term.isEmpty()) return SearchResults()

    return SearchResults(
        media = ENTRIES
            .filter { item ->
                item.media.title.contains(term, ignoreCase = true) ||
                    item.media.altText.contains(term, ignoreCase = true) ||
                    item.metadata.album.contains(term, ignoreCase = true) ||
                    item.metadata.artist.contains(term, ignoreCase = true) ||
                    item.metadata.genre.contains(term, ignoreCase = true)
            }
            .map { it.media },
        folders = FOLDERS.filter { folder ->
            folder.name.contains(term, ignoreCase = true) ||
                folder.path.contains(term, ignoreCase = true)
        },
        collections = COLLECTIONS.filter { collection ->
            collection.name.contains(term, ignoreCase = true) ||
                collection.description.contains(term, ignoreCase = true)
        },
    )
}