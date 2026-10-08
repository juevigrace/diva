package com.diva.app.feed.presentation.ui.components.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.feed.presentation.ui.components.FeedScreen
import com.diva.app.feed.presentation.viewmodel.FeedViewModel
import org.koin.compose.viewmodel.koinViewModel

fun EntryProviderScope<NavKey>.feedNav() {
    entry<FeedRoute> {
        val viewModel: FeedViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()
        FeedScreen(
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}
