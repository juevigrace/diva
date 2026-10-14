package com.diva.app.file.presentation.state

import com.diva.app.file.models.Directory

data class DirectoryState(
    val currentDirectoryId: String? = null,
    val breadcrumbs: List<Directory> = emptyList(),
    val entries: List<Directory> = emptyList(),
) {
    val isEmpty: Boolean
        get() = entries.isEmpty()

    /** Current directory title, falling back to the tab name at the root. */
    val title: String
        get() = breadcrumbs.lastOrNull()?.name ?: "Files"
}
