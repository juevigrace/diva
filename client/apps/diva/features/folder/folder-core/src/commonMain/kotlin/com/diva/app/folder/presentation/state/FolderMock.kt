package com.diva.app.folder.presentation.state

import com.diva.app.folder.models.Folder
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

private fun folder(id: String, name: String, path: String): Folder = Folder(
    id = id,
    user = OWNER,
    name = name,
    path = path,
)

private fun directory(
    id: String,
    name: String,
    path: String,
    childCount: Int,
): FolderEntry.Directory = FolderEntry.Directory(
    id = id,
    name = name,
    path = path,
    childCount = childCount,
)

private fun file(id: String, name: String, media: Media): FolderEntry.File =
    FolderEntry.File(id = id, name = name, media = media)

/**
 * A single flat map of folder id to its contents, standing in for the recursive
 * lookups the repository will provide. TODO(ui-pass): replace with
 * FolderRepository.getRoots/getChildren/getMediaByFolder.
 */
private val CONTENTS: Map<String, List<FolderEntry>> = mapOf(
    "" to listOf(
        directory("f-music", "Music", "/Music", 3),
        directory("f-videos", "Videos", "/Videos", 2),
        directory("f-pictures", "Pictures", "/Pictures", 2),
        directory("f-docs", "Documents", "/Documents", 1),
    ),
    "f-music" to listOf(
        directory("f-albums", "Albums", "/Music/Albums", 2),
        directory("f-live", "Live Sets", "/Music/Live Sets", 2),
        directory("f-dl", "Downloads", "/Music/Downloads", 3),
        file("f-file-1", "midnight-drive.mp3", media("m-01", "Midnight Drive", 254_000L)),
        file("f-file-2", "neon-coastline.mp3", media("m-02", "Neon Coastline", 198_000L)),
    ),
    "f-albums" to listOf(
        directory("f-after-hours", "After Hours", "/Music/Albums/After Hours", 2),
        directory("f-soft-focus", "Soft Focus", "/Music/Albums/Soft Focus", 2),
    ),
    "f-after-hours" to listOf(
        file("m-01", "midnight-drive.mp3", media("m-01", "Midnight Drive", 254_000L)),
        file("m-02", "neon-coastline.mp3", media("m-02", "Neon Coastline", 198_000L)),
    ),
    "f-soft-focus" to listOf(
        file("m-03", "paper-lanterns.mp3", media("m-03", "Paper Lanterns", 312_000L)),
        file("m-04", "static-bloom.mp3", media("m-04", "Static Bloom", 227_000L)),
    ),
    "f-live" to listOf(
        file("v-01", "studio-session.mp4", media("v-01", "Studio Session Reel", 512_000L, MediaType.VIDEO)),
    ),
    "f-dl" to listOf(
        file("m-05", "long-way-home.mp3", media("m-05", "Long Way Home", 285_000L)),
        file("m-06", "harbour-lights.mp3", media("m-06", "Harbour Lights", 241_000L)),
        file("m-07", "slow-tide.mp3", media("m-07", "Slow Tide", 203_000L)),
    ),
    "f-videos" to listOf(
        file("v-01", "studio-session.mp4", media("v-01", "Studio Session Reel", 512_000L, MediaType.VIDEO)),
        file("v-02", "coastal-fog.mp4", media("m-09", "Coastal Fog", 219_000L, MediaType.VIDEO)),
    ),
    "f-pictures" to listOf(
        file("p-01", "cover.png", media("p-01", "Cover Art", 0L, MediaType.IMAGE)),
        file("p-02", "backdrop.png", media("p-02", "Backdrop", 0L, MediaType.IMAGE)),
    ),
    "f-docs" to listOf(
        file("d-01", "liner-notes.pdf", media("d-01", "Liner Notes", 0L)),
    ),
)

/** Parent of every folder, "" being the filesystem root. */
private val PARENT: Map<String, String> = mapOf(
    "f-music" to "",
    "f-videos" to "",
    "f-pictures" to "",
    "f-docs" to "",
    "f-albums" to "f-music",
    "f-live" to "f-music",
    "f-dl" to "f-music",
    "f-after-hours" to "f-albums",
    "f-soft-focus" to "f-albums",
)

private val LABELS: Map<String, String> = mapOf(
    "f-music" to "Music",
    "f-videos" to "Videos",
    "f-pictures" to "Pictures",
    "f-docs" to "Documents",
    "f-albums" to "Albums",
    "f-live" to "Live Sets",
    "f-dl" to "Downloads",
    "f-after-hours" to "After Hours",
    "f-soft-focus" to "Soft Focus",
)

internal fun folderEntries(folderId: String): List<FolderEntry> =
    CONTENTS[folderId].orEmpty()

/** Root -> current, used to build the breadcrumb trail. */
internal fun folderBreadcrumbs(folderId: String?): List<Folder> {
    if (folderId == null) return emptyList()
    val trail = mutableListOf(folderId)
    var parent = PARENT[folderId]
    while (parent != null && parent.isNotEmpty()) {
        trail.add(parent)
        parent = PARENT[parent]
    }
    return trail.reversed().map {
        folder(it, LABELS.getValue(it), "/" + LABELS.getValue(it))
    }
}