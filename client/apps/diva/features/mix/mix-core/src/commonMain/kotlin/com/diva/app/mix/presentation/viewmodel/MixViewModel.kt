package com.diva.app.mix.presentation.viewmodel

import com.diva.app.mix.presentation.events.MixEvents
import com.diva.app.mix.presentation.state.MixState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MixViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<MixState>
        field = MutableStateFlow(MixState())

    fun onEvent(event: MixEvents) {
        when (event) {
            MixEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}