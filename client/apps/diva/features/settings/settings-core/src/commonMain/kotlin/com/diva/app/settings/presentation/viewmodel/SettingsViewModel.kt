package com.diva.app.settings.presentation.viewmodel

import com.diva.app.settings.domain.SettingsRepository
import com.diva.app.settings.presentation.events.SettingsEvents
import com.diva.app.settings.presentation.state.SettingsState
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.settings.presentation.viewmodel.SharedSettingsViewModel
import io.github.juevigrace.diva.ui.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    override val repository: SettingsRepository,
    override val navigator: Navigator,
) : SharedSettingsViewModel<SettingsRepository, SettingsState>(
    repository,
    navigator
) {
    override val state: StateFlow<SettingsState>
        field = MutableStateFlow(SettingsState())

    init {
        scope.launch { observeSettings() }
    }

    fun onEvent(event: SettingsEvents) {
        when (event) {
            SettingsEvents.OnBack -> onBack()
        }
    }

    private suspend fun observeSettings() {
        repository.observe().collect { result ->
            result.map { settings ->
                state.update { state ->
                    state.copy(settings = Option.of(settings))
                }
            }
        }
    }
}
