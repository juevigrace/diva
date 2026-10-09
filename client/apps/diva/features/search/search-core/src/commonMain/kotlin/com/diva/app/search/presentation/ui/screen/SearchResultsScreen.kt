package com.diva.app.search.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import com.diva.app.search.presentation.state.collectionTypeTag
import com.diva.app.search.presentation.state.folderTag
import com.diva.app.search.presentation.state.mediaTypeTag
import com.diva.app.search.presentation.ui.components.IconResultRow
import com.diva.app.search.presentation.ui.components.SearchField
import com.diva.app.search.presentation.ui.components.SectionHeader
import com.diva.app.ui.components.Artwork
import com.diva.app.ui.components.ArtworkPlaceholder
import com.diva.app.ui.components.TypeBadge
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.window.rememberWindowInfo

/**
 * The search results destination. Owns the query field as its top bar, so [onBack] is the
 * only way out and the field keeps the query while the user walks results.
 */
@Composable
fun SearchResultsScreen(
    state: SearchState,
    onEvent: (SearchEvents) -> Unit,
    onBack: () -> Unit,
) {
    val results = state.results
    val windowInfo = rememberWindowInfo()
    val isExpanded = windowInfo.widthSizeClass == WindowWidthSizeClass.Expanded

    Screen(
        topBar = {
            SearchField(
                state = state,
                onEvent = onEvent,
                requestFocus = !isExpanded,
                modifier = Modifier.padding(top = 8.dp),
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            if (state.isQueryEmpty) {
                item {
                    Text(
                        text = "Try searching for",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 8.dp),
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(state.suggestions, key = { it }) { suggestion ->
                            AssistChip(
                                onClick = {
                                    onEvent(SearchEvents.OnSelectSuggestion(suggestion))
                                },
                                label = { Text(suggestion) },
                            )
                        }
                    }
                }
            } else {
                if (results.isEmpty) {
                    item {
                        Text(
                            text = "No results for \"${state.query}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(24.dp),
                        )
                    }
                }

                if (results.media.isNotEmpty()) {
                    item { SectionHeader(title = "Media", count = results.media.size) }
                    items(results.media, key = { it.id }) { item ->
                        val tag = mediaTypeTag(item.mediaType)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onEvent(SearchEvents.OnOpenMedia(item)) }
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Artwork(
                                modifier = Modifier.size(44.dp),
                                shape = MaterialTheme.shapes.small,
                            ) {
                                ArtworkPlaceholder(alt = item.title, modifier = Modifier.fillMaxSize())
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 12.dp),
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    TypeBadge(label = tag.label, icon = tag.icon)
                                    if (item.mimeType.isNotBlank()) {
                                        Text(
                                            text = item.mimeType,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                if (results.collections.isNotEmpty()) {
                    item { SectionHeader(title = "Collections", count = results.collections.size) }
                    items(results.collections, key = { it.id }) { item ->
                        IconResultRow(
                            tag = collectionTypeTag(item.collectionType),
                            title = item.name,
                            subtitle = item.description,
                            onClick = { onEvent(SearchEvents.OnOpenCollection(item.id)) },
                        )
                    }
                }
                if (results.folders.isNotEmpty()) {
                    item { SectionHeader(title = "Folders", count = results.folders.size) }
                    items(results.folders, key = { it.id }) { item ->
                        IconResultRow(
                            tag = folderTag,
                            title = item.name,
                            subtitle = item.path,
                            onClick = { onEvent(SearchEvents.OnOpenFolder(item.id)) },
                        )
                    }
                }
            }
        }
    }
}
