package com.diva.app.search.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.search.presentation.ui.screen.SearchScreen
import com.diva.app.search.presentation.viewmodel.SearchViewModel

/**
 * The view model is hoisted by the tab host rather than resolved per entry, so the
 * desktop top-bar search bar and both search screens drive the same query state.
 *
 * [SearchRoute] is the mobile tab root; [SearchResultsRoute] is the destination the
 * desktop bar pushes when a result is picked from its preview.
 */
fun EntryProviderScope<NavKey>.searchNav(viewModel: SearchViewModel) {
    entry<SearchRoute> {
        SearchScreen(viewModel = viewModel)
    }
    entry<SearchResultsRoute> {
        SearchScreen(viewModel = viewModel, showSearchField = false)
    }
}
