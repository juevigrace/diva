package com.diva.app.features.home.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.features.home.presentation.ui.screen.HomeScreen
import com.diva.app.ui.navigation.HomeRoute

fun EntryProviderScope<NavKey>.homeNav() {
    entry<HomeRoute> {
        HomeScreen()
    }
}
