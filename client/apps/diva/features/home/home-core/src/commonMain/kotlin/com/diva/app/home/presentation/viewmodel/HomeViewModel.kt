package com.diva.app.home.presentation.viewmodel

import com.diva.app.home.presentation.events.HomeEvents
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.home.presentation.state.mockHomeState
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.player.presentation.ui.components.navigation.PlayerRoute
import com.diva.app.search.presentation.ui.components.navigation.SearchRoute
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val navigator: Navigator,
    private val tabNavigator: TabNavigator,
) : DivaViewModel() {

    // TODO(ui-pass): restore repository wiring
    val state: StateFlow<HomeState>
        field = MutableStateFlow(mockHomeState())

    fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnBack -> onBack()
            is HomeEvents.OnOpenMedia -> navigator.navigate(
                PlayerRoute(mediaType = event.media.mediaType)
            )
            HomeEvents.OnOpenSearch -> tabNavigator.selectTab(SearchRoute)
            HomeEvents.OnOpenLibrary -> tabNavigator.selectTab(LibraryRoute)
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}