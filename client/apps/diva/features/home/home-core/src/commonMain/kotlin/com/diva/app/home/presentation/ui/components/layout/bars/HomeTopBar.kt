package com.diva.app.home.presentation.ui.components.layout.bars

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.diva.app.folder.presentation.ui.components.navigation.FolderRoute
import com.diva.app.folder.presentation.ui.components.navigation.FoldersRoute
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.folders
import com.diva.app.generated.resources.home
import com.diva.app.generated.resources.library
import com.diva.app.generated.resources.profile
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import com.diva.app.search.presentation.ui.components.SearchField
import com.diva.app.search.presentation.ui.components.navigation.SearchResultsRoute
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    topEntry: NavKey?,
    isExpanded: Boolean,
    searchState: SearchState,
    onSearchEvent: (SearchEvents) -> Unit,
    onBack: () -> Unit,
    onOpenSearch: () -> Unit,
    folderTitle: String,
) {
    when (topEntry) {
        HomeRoute -> if (isExpanded) {
            SearchField(
                state = searchState,
                onEvent = onSearchEvent,
                modifier = Modifier.padding(top = 8.dp),
            )
        } else {
            TopAppBar(
                title = { Text(stringResource(Res.string.home)) },
                actions = {
                    IconButton(onClick = onOpenSearch) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                },
            )
        }

        SearchResultsRoute -> SearchField(
            state = searchState,
            onEvent = onSearchEvent,
            onBack = onBack,
            requestFocus = !isExpanded,
            modifier = Modifier.padding(top = 8.dp),
        )

        FoldersRoute -> TopAppBar(title = { Text(stringResource(Res.string.folders)) })

        is FolderRoute -> TopAppBar(
            title = { Text(folderTitle) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
        )

        LibraryRoute -> TopAppBar(title = { Text(stringResource(Res.string.library)) })

        ProfileRoute -> TopAppBar(title = { Text(stringResource(Res.string.profile)) })

        else -> {}
    }
}