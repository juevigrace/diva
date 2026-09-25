package com.diva.app.home.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.home.presentation.ui.screen.HomeScreen

fun EntryProviderScope<NavKey>.homeNav() {
    entry<HomeRoute> {
        HomeScreen()
    }
}
