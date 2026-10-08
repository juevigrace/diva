package com.diva.app.search.presentation.ui.components.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.search.presentation.ui.screen.SearchResultsScreen
import com.diva.app.search.presentation.viewmodel.SearchViewModel
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator

fun EntryProviderScope<NavKey>.searchNav(viewModel: SearchViewModel) {
    entry<SearchResultsRoute> {
        val state by viewModel.state.collectAsStateWithLifecycle()
        val tabNavigator = LocalTabNavigator.current
        SearchResultsScreen(
            state = state,
            onEvent = viewModel::onEvent,
            onBack = { tabNavigator.pop() },
        )
    }
}
