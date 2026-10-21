package com.diva.app.search.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import com.diva.app.search.presentation.ui.components.SearchResultsList
import com.diva.app.search.presentation.viewmodel.SearchViewModel
import io.github.juevigrace.diva.lib.ui.components.input.SearchField
import io.github.juevigrace.diva.lib.ui.components.input.SearchFieldEvents
import io.github.juevigrace.diva.ui.layout.Screen

/**
 * Search content shared by the mobile tab and the desktop pushed destination. With an
 * empty query it shows suggestions and recommended rows; results replace them live as
 * the user types. The view model is hoisted by the tab host so the desktop top-bar
 * search bar drives the same query.
 *
 * @param showSearchField whether the screen renders its own field. The mobile tab owns
 *   it; the desktop destination is pushed from the top bar, which already has one.
 */
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    showSearchField: Boolean = true,
) {
    val state: SearchState by viewModel.state.collectAsStateWithLifecycle()
    val results = state.displayed

    Screen(
        topBar = {
            if (showSearchField) {
                SearchField(
                    modifier = Modifier.padding(top = 8.dp),
                    value = state.query,
                    onEvent = { event ->
                        when (event) {
                            is SearchFieldEvents.OnQueryChange ->
                                viewModel.onEvent(SearchEvents.OnQueryChange(event.query))
                            SearchFieldEvents.OnClear -> viewModel.onEvent(SearchEvents.OnClear)
                            SearchFieldEvents.OnSearch -> Unit
                        }
                    },
                )
            }
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
                                    viewModel.onEvent(SearchEvents.OnSelectSuggestion(suggestion))
                                },
                                label = { Text(suggestion) },
                            )
                        }
                    }
                }
                item {
                    Text(
                        text = "Recommended",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 8.dp),
                    )
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
            }

            SearchResultsList(
                results = results,
                onOpenMedia = { viewModel.onEvent(SearchEvents.OnOpenMedia(it)) },
                onOpenCollection = { viewModel.onEvent(SearchEvents.OnOpenCollection(it)) },
                onOpenFolder = { viewModel.onEvent(SearchEvents.OnOpenFolder(it)) },
            )
        }
    }
}
