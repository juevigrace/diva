package com.diva.app.home.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.home.presentation.events.HomeEvents
import com.diva.app.home.presentation.state.HomeSection
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.media.models.Media
import com.diva.app.player.presentation.ui.util.durationLabel
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import com.diva.app.search.presentation.ui.components.SearchField
import com.diva.app.ui.components.Artwork
import io.github.juevigrace.diva.lib.ui.components.carousel.Carousel
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.window.rememberWindowInfo
import org.koin.compose.viewmodel.koinViewModel

/**
 * The home tab root. Named `HomeContent` rather than `HomeScreen` because
 * [com.diva.app.home.presentation.ui.screen.HomeScreen] is the tab host.
 *
 * The search state is a parameter because it is the same instance the search results
 * destination renders, so a query typed here survives the push. The home view model is
 * resolved here instead, since nothing else reads it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    searchState: SearchState,
    onSearchEvent: (SearchEvents) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val windowInfo = rememberWindowInfo()

    Screen(
        topBar = {
            TopAppBar(
                title = {
                    if (windowInfo.widthSizeClass == WindowWidthSizeClass.Expanded) {
                        SearchField(
                            state = searchState,
                            onEvent = onSearchEvent,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    } else {
                        Text(text = "Home")
                    }
                },
                actions = {
                    if (windowInfo.widthSizeClass != WindowWidthSizeClass.Expanded) {
                        IconButton(onClick = { onEvent(HomeEvents.OnOpenSearch) }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                },
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            item {
                Column(modifier = Modifier.padding(start = 20.dp, top = 16.dp, end = 20.dp)) {
                    Text(
                        text = state.greeting,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AssistChip(
                        onClick = { onEvent(HomeEvents.OnOpenSearch) },
                        label = { Text("Search") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )
                    AssistChip(
                        onClick = { onEvent(HomeEvents.OnOpenLibrary) },
                        label = { Text("Your library") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.LibraryMusic,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                    )
                }
            }

            items(state.sections, key = { it.id }) { section ->
                HomeRow(section = section, onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun HomeRow(
    section: HomeSection,
    onEvent: (HomeEvents) -> Unit,
) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp, top = 8.dp, end = 20.dp, bottom = 8.dp),
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val spacing = 12.dp
            val gutter = 40.dp
            val visible = maxOf(
                1,
                ((maxWidth - gutter + spacing) / (150.dp + spacing)).toInt(),
            )
            val cardWidth = ((maxWidth - gutter - spacing * (visible - 1)) / visible)
                .coerceIn(96.dp, 150.dp)

            Carousel(
                pageCount = section.media.size,
                visiblePages = visible,
                pageSize = cardWidth,
                pageSpacing = spacing,
                modifier = Modifier.fillMaxWidth(),
            ) { page ->
                GalleryCard(
                    media = section.media[page],
                    width = cardWidth,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun GalleryCard(
    media: Media,
    width: Dp,
    onEvent: (HomeEvents) -> Unit,
) {
    Column(
        modifier = Modifier
            .width(width)
            .clickable { onEvent(HomeEvents.OnOpenMedia(media)) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        ) {
            Artwork(
                seed = media.title,
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.large,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = media.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = media.durationMs.durationLabel(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
