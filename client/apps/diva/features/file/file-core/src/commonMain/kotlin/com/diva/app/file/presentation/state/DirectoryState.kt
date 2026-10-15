package com.diva.app.file.presentation.state

import com.diva.app.file.models.Directory
import com.diva.app.file.models.FsEntry
import io.github.juevigrace.diva.core.Option

data class DirectoryState(
    val breadcrumbs: List<Directory> = emptyList(),
    val entries: List<FsEntry> = emptyList(),
) {
    val currentEntry: Option<Directory>
        get() = Option.of(breadcrumbs.lastOrNull())
}
