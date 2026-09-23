package com.diva.app.features.search.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.search.presentation.ui.screen.SearchScreen

fun EntryProviderScope<NavKey>.searchNav() {
    entry<SearchRoute> {
        SearchScreen()
    }
}