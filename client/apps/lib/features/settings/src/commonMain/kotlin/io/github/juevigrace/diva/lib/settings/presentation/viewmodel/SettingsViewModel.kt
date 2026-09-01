package io.github.juevigrace.diva.lib.settings.presentation.viewmodel

import io.github.juevigrace.diva.lib.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.settings.presentation.events.SettingsEvents
import io.github.juevigrace.diva.lib.settings.presentation.state.SettingsState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<SettingsState>
        field = MutableStateFlow(SettingsState())

    fun onEvent(event: SettingsEvents) {
        when (event) {
            SettingsEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
