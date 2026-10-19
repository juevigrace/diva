package io.github.juevigrace.diva.lib.file.presentation.state

import io.github.juevigrace.diva.lib.file.models.DeviceFile
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.FileCategory
import io.github.juevigrace.diva.lib.file.models.FsEntry
import io.github.juevigrace.diva.core.Option

data class DirectoryState(
    val breadcrumbs: List<Directory> = emptyList(),
    val entries: List<FsEntry> = emptyList(),
    val category: FileCategory? = null,
    val selectedIds: Set<String> = emptySet(),
    val loadFailed: Boolean = false,
    val showPermissionDialog: Boolean = false,
    val permissionMissing: Boolean = false,
) {
    val currentEntry: Option<Directory>
        get() = Option.of(breadcrumbs.lastOrNull())

    /** Entries narrowed to the active [category]; directories always pass. */
    val visibleEntries: List<FsEntry>
        get() = if (category == null) {
            entries
        } else {
            entries.filter { it is Directory || (it as? DeviceFile)?.category == category }
        }

    /** True while the contextual selection bar should be shown. */
    val selectionActive: Boolean
        get() = selectedIds.isNotEmpty()

    /**
     * Selected entries resolved against the current listing, in listing order.
     * Ids that no longer resolve (e.g. deleted during a rescan) are dropped.
     */
    val selectedEntries: List<FsEntry>
        get() = entries.filter { it.id in selectedIds }

    /** True when every currently visible entry is selected (drives select-all). */
    val allVisibleSelected: Boolean
        get() = visibleEntries.isNotEmpty() && visibleEntries.all { it.id in selectedIds }
}
