package io.github.juevigrace.diva.lib.file.presentation.actions

import androidx.compose.ui.graphics.vector.ImageVector
import io.github.juevigrace.diva.lib.file.models.FsEntry

/**
 * A screen-level action declared by the consumer of the files screen.
 *
 * The files module owns the selection state; the consumer owns the behaviour.
 * Actions are rendered in the selection bar's overflow menu and only ever see
 * the currently selected entries (files and directories alike), so a consumer
 * can implement things like "play selected" or "create playlist from folder"
 * without the module knowing anything about playback or playlists.
 */
data class FileAction(
    val label: String,
    val icon: ImageVector? = null,
    /** Renders the item disabled when false; evaluated against the live selection. */
    val isEnabled: (List<FsEntry>) -> Boolean = { true },
    /** Invoked with the selected entries; the screen clears the selection afterwards. */
    val onClick: (List<FsEntry>) -> Unit,
)
