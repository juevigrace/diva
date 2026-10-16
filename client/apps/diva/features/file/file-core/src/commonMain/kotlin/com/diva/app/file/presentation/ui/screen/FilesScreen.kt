package com.diva.app.file.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.file.models.Directory
import com.diva.app.file.models.FileCategory
import com.diva.app.file.presentation.events.DirectoryEvents
import com.diva.app.file.presentation.state.DirectoryState
import com.diva.app.file.presentation.viewmodel.FilesViewModel
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FilesScreen(
    viewModel: FilesViewModel = koinViewModel(),
) {
    val state: DirectoryState by viewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = state.breadcrumbs.isNotEmpty()) {
        viewModel.onEvent(DirectoryEvents.OnNavigateUp)
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = isDirectory) {
                            viewModel.onEvent(DirectoryEvents.OnOpenDirectory(entry.id))
                        }
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
    }
}
