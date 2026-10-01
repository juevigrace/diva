package com.diva.app.library.presentation.state

import com.diva.app.folder.models.Folder
import com.diva.app.media.models.Media

enum class LibraryFilter(val label: String) {
    ALL("All"),
    FAVORITES("Favourites"),
    RECENT("Recent"),
    RESUMABLE("Resume"),
    FOLDERS("Folders"),
}

data class LibrarySection(
    val id: String,
    val title: String,
    val media: List<Media>,
)

data class LibraryState(
    val filter: LibraryFilter = LibraryFilter.ALL,
    val sections: List<LibrarySection> = emptyList(),
    val folders: List<Folder> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
) {
    val isEmpty: Boolean
        get() = sections.all { it.media.isEmpty() } && folders.isEmpty()
}