package com.diva.app.media.presentation.viewmodel

import com.diva.app.media.presentation.events.MediaEvents
import com.diva.app.media.presentation.state.MediaState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<MediaState>
        field = MutableStateFlow(MediaState())

    fun onEvent(event: MediaEvents) {
        when (event) {
            MediaEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}