package com.diva.app.library.presentation.ui.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.library
import com.diva.app.library.presentation.events.LibraryEvents
import com.diva.app.library.presentation.state.LibraryFilter
import com.diva.app.library.presentation.viewmodel.LibraryViewModel
import com.diva.app.player.presentation.ui.util.durationLabel
import com.diva.app.ui.components.CarouselCard
import com.diva.app.ui.components.CarouselSection
import io.github.juevigrace.diva.ui.layout.Screen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * The library tab root. Nothing else reads the library state, so the view model is
 * resolved here rather than hoisted by the tab host.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    Screen(
        topBar = { TopAppBar(title = { Text(stringResource(Res.string.library)) }) },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(LibraryFilter.entries.toList(), key = { it.name }) { filter ->
                        FilterChip(
                            selected = state.filter == filter,
                            onClick = { onEvent(LibraryEvents.OnFilterChange(filter)) },
                            label = { Text(filter.label) },
                        )
                    }
                }
            }

            if (state.filter == LibraryFilter.FOLDERS) {
                item {
                    Text(
                        text = "Folders",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }
                items(state.folders, key = { it.id }) { folder ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEvent(LibraryEvents.OnOpenFolder(folder.id)) }
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
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
                            Text(text = folder.name, style = MaterialTheme.typography.bodyLarge)
                            Text(
                                text = folder.path,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            items(state.sections, key = { it.id }) { section ->
                CarouselSection(
                    title = {
                        Text(
                            text = section.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    items = section.media,
                ) { media, modifier ->
                    CarouselCard(
                        alt = media.title,
                        title = media.title,
                        subtitle = media.durationMs.durationLabel(),
                        tag = media.mediaType.name.lowercase(),
                        tagIcon = Icons.Filled.PlayArrow,
                        isFavorite = media.id in state.favoriteIds,
                        onToggleFavorite = { onEvent(LibraryEvents.OnToggleFavorite(media)) },
                        onClick = { onEvent(LibraryEvents.OnOpenMedia(media)) },
                        modifier = modifier,
                    )
                }
            }

            if (state.isEmpty) {
                item {
                    Text(
                        text = "Nothing here yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(24.dp),
                    )
                }
            }
        }
    }
}
