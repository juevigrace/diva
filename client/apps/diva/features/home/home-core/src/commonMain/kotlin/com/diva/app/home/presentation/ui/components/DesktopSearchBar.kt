package com.diva.app.home.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExpandedDockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.search
import com.diva.app.search.models.SearchResults
import com.diva.app.search.presentation.ui.components.SearchResultsList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/** How many rows of each kind the desktop preview shows before "See all results". */
private const val PreviewLimitPerSection = 3

/**
 * Desktop-only search entry point. Lives in the top bar and expands into a docked
 * popup with a preview of the results; picking a row, pressing search, or tapping
 * "See all results" collapses the popup and asks the caller to push the full results
 * destination on the currently selected tab.
 *
 * Presentation only: the query lives in the hoisted search view model, so both this
 * bar and the pushed search screen render the same state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopSearchBar(
    query: String,
    results: SearchResults,
    isQueryEmpty: Boolean,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onOpenResults: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()

    // Row selection should navigate immediately; snap the popup shut instead of
    // animating it so it does not linger over the incoming screen.
    val openResults = {
        scope.launch { searchBarState.snapTo(0f) }
        onOpenResults()
    }

    val colors = SearchBarDefaults.colors(
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    )

    val inputField: @Composable () -> Unit = {
        SearchBarDefaults.InputField(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = { openResults() },
            expanded = searchBarState.currentValue == SearchBarValue.Expanded,
            onExpandedChange = { expanded ->
                scope.launch {
                    if (expanded) {
                        searchBarState.animateToExpanded()
                    } else {
                        searchBarState.animateToCollapsed()
                    }
                }
            },
            placeholder = { Text(text = stringResource(Res.string.search)) },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear",
                        )
                    }
                }
            },
            colors = colors.inputFieldColors,
        )
    }

    SearchBar(
        state = searchBarState,
        modifier = modifier,
        colors = colors,
        inputField = inputField,
    )

    ExpandedDockedSearchBar(
        state = searchBarState,
        colors = colors,
        inputField = inputField,
    ) {
        if (results.isEmpty && !isQueryEmpty) {
            Text(
                text = "No results for \"$query\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(20.dp),
            )
        } else {
            LazyColumn {
                if (isQueryEmpty) {
                    item {
                        Text(
                            text = "Recommended",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(
                                start = 20.dp,
                                top = 16.dp,
                                end = 20.dp,
                                bottom = 4.dp,
                            ),
                        )
                    }
                }
                SearchResultsList(
                    results = results,
                    limitPerSection = PreviewLimitPerSection,
                    onOpenMedia = { openResults() },
                    onOpenCollection = { openResults() },
                    onOpenFolder = { openResults() },
                )
                item {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { openResults() }
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "See all results",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
