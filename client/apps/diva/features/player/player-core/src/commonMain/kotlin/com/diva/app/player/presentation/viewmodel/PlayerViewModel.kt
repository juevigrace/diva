package com.diva.app.player.presentation.viewmodel

import com.diva.app.player.presentation.events.PlayerEvents
import com.diva.app.player.presentation.state.PlayerState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<PlayerState>
        field = MutableStateFlow(PlayerState())

    fun onEvent(event: PlayerEvents) {
        when (event) {
            PlayerEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}