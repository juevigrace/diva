package com.diva.app.search.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import com.diva.app.search.presentation.state.TypeTag
import com.diva.app.search.presentation.state.collectionTypeTag
import com.diva.app.search.presentation.state.folderTag
import com.diva.app.search.presentation.state.mediaTypeTag
import com.diva.app.ui.components.Artwork
import com.diva.app.ui.components.TypeBadge

@Composable
fun SearchResultsContent(
    state: SearchState,
    onEvent: (SearchEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    val results = state.results

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
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
                    MediaResultRow(
                        title = item.title,
                        subtitle = item.mimeType,
                        tag = mediaTypeTag(item.mediaType),
                        onClick = { onEvent(SearchEvents.OnOpenMedia(item)) },
                    )
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

@Composable
private fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun MediaResultRow(
    title: String,
    subtitle: String,
    tag: TypeTag,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Artwork(
            seed = title,
            modifier = Modifier.size(44.dp),
            shape = MaterialTheme.shapes.small,
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TypeBadge(label = tag.label, icon = tag.icon)
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
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

@Composable
private fun IconResultRow(
    tag: TypeTag,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TypeBadge(
            label = tag.label,
            icon = tag.icon,
            modifier = Modifier.size(width = 84.dp, height = 32.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}