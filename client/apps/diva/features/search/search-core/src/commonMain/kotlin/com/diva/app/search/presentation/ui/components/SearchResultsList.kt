package com.diva.app.search.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.diva.app.media.models.Media
import com.diva.app.search.models.SearchResults
import com.diva.app.search.presentation.state.collectionTypeTag
import com.diva.app.search.presentation.state.folderTag
import com.diva.app.search.presentation.state.mediaTypeTag
import com.diva.app.ui.components.Artwork
import com.diva.app.ui.components.ArtworkPlaceholder
import com.diva.app.ui.components.TypeBadge

/**
 * The grouped result rows shared by the search screen and the desktop search preview.
 * Section headers always show the total counts; [limitPerSection] only caps how many
 * rows of each kind are emitted, so the top-bar dropdown can show a short preview.
 */
fun LazyListScope.SearchResultsList(
    results: SearchResults,
    onOpenMedia: (Media) -> Unit,
    onOpenCollection: (String) -> Unit,
    onOpenFolder: (String) -> Unit,
    limitPerSection: Int? = null,
) {
    val media = limitPerSection?.let { results.media.take(it) } ?: results.media
    val collections = limitPerSection?.let { results.collections.take(it) } ?: results.collections
    val folders = limitPerSection?.let { results.folders.take(it) } ?: results.folders

    if (media.isNotEmpty()) {
        item { SectionHeader(title = "Media", count = results.media.size) }
        items(media, key = { it.id }) { item ->
            val tag = mediaTypeTag(item.mediaType)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenMedia(item) }
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
    if (collections.isNotEmpty()) {
        item { SectionHeader(title = "Collections", count = results.collections.size) }
        items(collections, key = { it.id }) { item ->
            IconResultRow(
                tag = collectionTypeTag(item.collectionType),
                title = item.name,
                subtitle = item.description,
                onClick = { onOpenCollection(item.id) },
            )
        }
    }
    if (folders.isNotEmpty()) {
        item { SectionHeader(title = "Folders", count = results.folders.size) }
        items(folders, key = { it.id }) { item ->
            IconResultRow(
                tag = folderTag,
                title = item.name,
                subtitle = item.path,
                onClick = { onOpenFolder(item.id) },
            )
        }
    }
}
