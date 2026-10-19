package io.github.juevigrace.diva.lib.file.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.lib.file.models.Directory
import io.github.juevigrace.diva.lib.file.models.FileCategory
import io.github.juevigrace.diva.lib.file.presentation.actions.FileAction
import io.github.juevigrace.diva.lib.file.presentation.events.DirectoryEvents
import io.github.juevigrace.diva.lib.file.presentation.state.DirectoryState
import io.github.juevigrace.diva.lib.file.presentation.ui.components.selection.DragSelectionState
import io.github.juevigrace.diva.lib.file.presentation.ui.components.selection.selectionEntry
import io.github.juevigrace.diva.lib.file.presentation.viewmodel.FilesViewModel
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import org.koin.compose.viewmodel.koinViewModel

/**
 * Files and folders browser.
 *
 * Selection lives entirely inside the module. A consumer that wants to act on
 * selected entries declares [FileAction]s; they appear in the selection bar and
 * receive the selected entries when tapped, so no playback or playlist concept
 * ever leaks into the file feature.
 */
@Composable
fun FilesScreen(
    actions: List<FileAction> = emptyList(),
    viewModel: FilesViewModel = koinViewModel(),
) {
    val state: DirectoryState by viewModel.state.collectAsStateWithLifecycle()
    val dragState = remember { DragSelectionState() }

    val onToggleEntry: (String) -> Unit = { entryId ->
        viewModel.onEvent(DirectoryEvents.OnToggleSelect(entryId))
    }
    val onEntryTap: (String) -> Unit = { entryId ->
        val current = viewModel.state.value
        when {
            current.selectionActive -> viewModel.onEvent(DirectoryEvents.OnToggleSelect(entryId))
            current.entries.firstOrNull { it.id == entryId } is Directory ->
                viewModel.onEvent(DirectoryEvents.OnOpenDirectory(entryId))
        }
    }

    BackHandler(enabled = state.selectionActive || state.breadcrumbs.isNotEmpty()) {
        if (state.selectionActive) {
            viewModel.onEvent(DirectoryEvents.OnClearSelection)
        } else {
            viewModel.onEvent(DirectoryEvents.OnNavigateUp)
        }
    }

    if (state.showPermissionDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(DirectoryEvents.OnPermissionDismiss) },
            title = { Text("File access needed") },
            text = {
                Text(
                    "Diva needs access to your files to list folders and show your media. " +
                        "Access stays on your device — nothing is uploaded.",
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(DirectoryEvents.OnPermissionConfirm) }) {
                    Text("Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(DirectoryEvents.OnPermissionDismiss) }) {
                    Text("Not now")
                }
            },
        )
    }

    Screen(
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                stickyHeader {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(state.breadcrumbs, key = { it.id }) { directory ->
                            Text(
                                text = directory.name,
                                style = MaterialTheme.typography.labelLarge,
                                color = state.currentEntry.fold(
                                    onNone = { MaterialTheme.colorScheme.primary },
                                    onSome = { entry ->
                                        if (entry.id == directory.id) {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable { viewModel.onEvent(DirectoryEvents.OnNavigateToBreadcrumb(directory.id)) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                            )
                        }
                    }
                }

                stickyHeader {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        item(key = "all") {
                            FilterChip(
                                selected = state.category == null,
                                onClick = { viewModel.onEvent(DirectoryEvents.OnSetCategory(null)) },
                                label = { Text("All") },
                            )
                        }
                        items(FileCategory.entries, key = { it.name }) { category ->
                            FilterChip(
                                selected = state.category == category,
                                onClick = { viewModel.onEvent(DirectoryEvents.OnSetCategory(category)) },
                                label = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            )
                        }
                    }
                }

                if (state.permissionMissing && state.visibleEntries.isEmpty()) {
                    item(key = "permission-missing") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = "Storage permission is not granted",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                            )
                            Text(
                                text = "Files and folders cannot be listed until access is granted.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                items(state.visibleEntries, key = { it.id }) { entry ->
                    val isDirectory = entry is Directory
                    val selected = entry.id in state.selectedIds
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectionEntry(
                                entryId = entry.id,
                                dragState = dragState,
                                selectedIds = { viewModel.state.value.selectedIds },
                                onTap = onEntryTap,
                                onToggle = onToggleEntry,
                            )
                            .background(
                                if (selected) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    Color.Transparent
                                },
                            )
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (state.selectionActive) {
                            // Pinned to the row icon's size so rows keep their
                            // height when selection starts mid-drag.
                            Checkbox(
                                checked = selected,
                                onCheckedChange = { onToggleEntry(entry.id) },
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Icon(
                            imageVector = if (isDirectory) {
                                Icons.Filled.Folder
                            } else {
                                Icons.AutoMirrored.Filled.InsertDriveFile
                            },
                            contentDescription = null,
                            tint = if (isDirectory) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.Top),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            Text(
                                text = entry.name,
                                style = MaterialTheme.typography.bodyLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = entry.path,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }

            if (state.selectionActive) {
                SelectionBar(
                    state = state,
                    actions = actions,
                    onClearSelection = { viewModel.onEvent(DirectoryEvents.OnClearSelection) },
                    onSelectAll = { viewModel.onEvent(DirectoryEvents.OnSelectAll) },
                    modifier = Modifier.align(Alignment.TopStart),
                )
            }
        }
    }
}

/**
 * Contextual bar shown while entries are selected. It overlays the top of the
 * list instead of taking part in the layout so starting a drag never shifts the
 * rows underneath the finger.
 */
@Composable
private fun SelectionBar(
    state: DirectoryState,
    actions: List<FileAction>,
    onClearSelection: () -> Unit,
    onSelectAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val selected = state.selectedEntries
    // Scoped to the bar, so clearing the selection closes the menu for good.
    var menuOpen by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onClearSelection) {
                Icon(Icons.Filled.Close, contentDescription = "Clear selection")
            }
            Text(
                text = "${selected.size} selected",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp),
            )
            if (state.visibleEntries.isNotEmpty() && !state.allVisibleSelected) {
                IconButton(onClick = onSelectAll) {
                    Icon(Icons.Filled.SelectAll, contentDescription = "Select all")
                }
            }
            if (actions.isNotEmpty()) {
                Box {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Selected entries actions")
                    }
                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false },
                    ) {
                        actions.forEach { action ->
                            DropdownMenuItem(
                                text = { Text(action.label) },
                                leadingIcon = action.icon?.let { icon -> { Icon(icon, contentDescription = null) } },
                                enabled = action.isEnabled(selected),
                                onClick = {
                                    menuOpen = false
                                    action.onClick(selected)
                                    onClearSelection()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
