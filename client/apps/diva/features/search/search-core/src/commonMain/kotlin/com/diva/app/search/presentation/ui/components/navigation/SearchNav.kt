package com.diva.app.search.presentation.ui.components.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.search.presentation.ui.components.SearchResultsScreen
import com.diva.app.search.presentation.viewmodel.SearchViewModel
import io.github.juevigrace.diva.ui.navigation.LocalNavigator
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.searchNav() {
    entry<SearchResultsRoute> {
        val viewModel: SearchViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.current
        SearchResultsScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onBack = { navigator.pop() },
        )
    }
}
