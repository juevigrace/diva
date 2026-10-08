package com.diva.app.home.presentation.ui.components.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.screen.HomeScreen
import com.diva.app.home.presentation.viewmodel.HomeViewModel

fun EntryProviderScope<NavKey>.homeNav() {
    entry<HomeRoute> {
        HomeScreen()
    }
}

fun EntryProviderScope<NavKey>.homeTabNav(viewModel: HomeViewModel) {
    entry<HomeRoute> {
        val state by viewModel.state.collectAsStateWithLifecycle()
        HomeContent(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}
