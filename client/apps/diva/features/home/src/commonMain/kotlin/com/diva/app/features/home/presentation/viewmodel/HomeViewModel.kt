package com.diva.app.features.home.presentation.viewmodel

import com.diva.app.features.home.presentation.events.HomeEvents
import com.diva.app.features.home.presentation.state.HomeState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<HomeState>
        field = MutableStateFlow(HomeState())

    fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
