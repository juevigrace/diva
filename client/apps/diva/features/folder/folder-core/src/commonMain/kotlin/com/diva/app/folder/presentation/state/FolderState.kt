package com.diva.app.folder.presentation.state

import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media

/**
 * One row in the folders browser. Modelled as a closed set so the same state shape
 * can be produced either from the diva folder index or, later, from a platform file
 * system source, without the UI needing to know which one it is reading.
 */
sealed interface FolderEntry {
    val id: String
    val name: String

    data class Directory(
        override val id: String,
        override val name: String,
        val path: String,
        val childCount: Int,
    ) : FolderEntry

    data class File(
        override val id: String,
        override val name: String,
        val media: Media,
    ) : FolderEntry
}

enum class CollectionDraftType(val label: String) {
    PLAYLIST("Playlist"),
    MIX("Mix"),
    ALBUM("Album"),
    FAVORITES("Favourites"),
}

/** The pending "create a collection from this selection" sheet. */
data class CollectionDraft(
    val name: String = "",
    val type: CollectionDraftType = CollectionDraftType.PLAYLIST,
)

data class FolderState(
    val currentFolderId: String? = null,
    val breadcrumbs: List<Folder> = emptyList(),
    val entries: List<FolderEntry> = emptyList(),
    val selectedIds: Set<String> = emptyList(),
    val draft: CollectionDraft? = null,
) {
    val isSelectionMode: Boolean
        get() = selectedIds.isNotEmpty()

    val isEmpty: Boolean
        get() = entries.isEmpty()

    /** Current directory title, falling back to the tab name at the root. */
    val title: String
        get() = breadcrumbs.lastOrNull()?.name ?: "Folders"

    /** Parents only, in order, excluding the current directory. */
    val parents: List<Folder>
        get() = breadcrumbs.dropLast(1)
}