package com.diva.app.features.server.presentation.viewmodel

import com.diva.app.features.server.presentation.events.ServerEvents
import com.diva.app.features.server.presentation.state.ServerState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ServerViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<ServerState>
        field = MutableStateFlow(ServerState())

    fun onEvent(event: ServerEvents) {
        when (event) {
            ServerEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}