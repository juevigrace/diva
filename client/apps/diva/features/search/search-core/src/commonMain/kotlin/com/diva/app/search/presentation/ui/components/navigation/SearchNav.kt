package com.diva.app.search.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.search.presentation.ui.screen.SearchResultsScreen

fun EntryProviderScope<NavKey>.searchNav() {
    entry<SearchResultsRoute> {
        SearchResultsScreen()
    }
}
