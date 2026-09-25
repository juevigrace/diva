package com.diva.app.playlist.presentation.viewmodel

import com.diva.app.playlist.presentation.events.PlaylistEvents
import com.diva.app.playlist.presentation.state.PlaylistState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaylistViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<PlaylistState>
        field = MutableStateFlow(PlaylistState())

    fun onEvent(event: PlaylistEvents) {
        when (event) {
            PlaylistEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}