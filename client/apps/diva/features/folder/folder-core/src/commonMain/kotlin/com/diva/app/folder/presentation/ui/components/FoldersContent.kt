package com.diva.app.folder.presentation.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diva.app.folder.presentation.events.FolderEvents
import com.diva.app.folder.presentation.state.CollectionDraft
import com.diva.app.folder.presentation.state.CollectionDraftType
import com.diva.app.folder.presentation.state.FolderEntry
import com.diva.app.folder.presentation.state.FolderState
import com.diva.app.ui.components.Artwork

@Composable
fun FoldersContent(
    state: FolderState,
    onEvent: (FolderEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Breadcrumbs(state = state, onEvent = onEvent)

        if (state.isSelectionMode) {
            SelectionBar(state = state, onEvent = onEvent)
        }

        if (state.isEmpty) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "This folder is empty.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp, top = 4.dp),
            ) {
                items(state.entries, key = { it.id }) { entry ->
                    when (entry) {
                        is FolderEntry.Directory -> DirectoryRow(
                            entry = entry,
                            selected = entry.id in state.selectedIds,
                            selectionMode = state.isSelectionMode,
                            onEvent = onEvent,
                        )
                        is FolderEntry.File -> FileRow(
                            entry = entry,
                            selected = entry.id in state.selectedIds,
                            selectionMode = state.isSelectionMode,
                            onEvent = onEvent,
                        )
                    }
                }
            }
        }
    }

    state.draft?.let { draft ->
        CollectionDraftSheet(draft = draft, onEvent = onEvent)
    }
}

@Composable
private fun Breadcrumbs(
    state: FolderState,
    onEvent: (FolderEvents) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        item {
            Text(
                text = "Root",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .clip()
                    .clickable { onEvent(FolderEvents.OnNavigateToBreadcrumb("")) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
        items(state.breadcrumbs, key = { it.id }) { folder ->
            Text(
                text = folder.name,
                style = MaterialTheme.typography.labelLarge,
                color = if (folder.id == state.currentFolderId) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier
                    .clip()
                    .clickable { onEvent(FolderEvents.OnNavigateToBreadcrumb(folder.id)) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun SelectionBar(
    state: FolderState,
    onEvent: (FolderEvents) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "${state.selectedIds.size} selected",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = { onEvent(FolderEvents.OnClearSelection) }) {
                Text("Clear")
            }
            Button(onClick = { onEvent(FolderEvents.OnStartCollectionDraft) }) {
                Text("New collection")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DirectoryRow(
    entry: FolderEntry.Directory,
    selected: Boolean,
    selectionMode: Boolean,
    onEvent: (FolderEvents) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEvent(FolderEvents.OnOpenFolder(entry.id)) },
                onLongClick = {
                    onEvent(FolderEvents.OnStartSelection)
                    onEvent(FolderEvents.OnToggleSelection(entry.id))
                },
            )
            .background(
                if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selectionMode) {
            SelectionMark(selected = selected)
            Spacer(modifier = Modifier.width(12.dp))
        }
        Icon(
            imageVector = Icons.Filled.Folder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
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
        Text(
            text = "${entry.childCount}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileRow(
    entry: FolderEntry.File,
    selected: Boolean,
    selectionMode: Boolean,
    onEvent: (FolderEvents) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onEvent(FolderEvents.OnOpenFile(entry.media)) },
                onLongClick = {
                    onEvent(FolderEvents.OnStartSelection)
                    onEvent(FolderEvents.OnToggleSelection(entry.id))
                },
            )
            .background(
                if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background
            )
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selectionMode) {
            SelectionMark(selected = selected)
            Spacer(modifier = Modifier.width(12.dp))
        }
        Artwork(
            seed = entry.name,
            modifier = Modifier.size(40.dp),
            shape = MaterialTheme.shapes.small,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        ) {
            Text(
                text = entry.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = entry.media.mediaType.name.lowercase(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun SelectionMark(selected: Boolean) {
    Icon(
        imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
        contentDescription = null,
        tint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        modifier = Modifier.size(22.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CollectionDraftSheet(
    draft: CollectionDraft,
    onEvent: (FolderEvents) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = { onEvent(FolderEvents.OnDismissCollectionDraft) }) {
        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 32.dp)) {
            Text(
                text = "New collection",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = draft.name,
                onValueChange = { onEvent(FolderEvents.OnDraftNameChange(it)) },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Type",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(CollectionDraftType.entries.toList(), key = { it.name }) { type ->
                    AssistChip(
                        onClick = { onEvent(FolderEvents.OnDraftTypeChange(type)) },
                        label = { Text(type.label) },
                        leadingIcon = if (draft.type == type) {
                            {
                                Checkbox(
                                    checked = true,
                                    onCheckedChange = null,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        } else {
                            null
                        },
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { onEvent(FolderEvents.OnDismissCollectionDraft) }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onEvent(FolderEvents.OnConfirmCollectionDraft) },
                    enabled = draft.name.isNotBlank(),
                ) {
                    Text("Create")
                }
            }
        }
    }
}