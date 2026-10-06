package io.github.juevigrace.diva.lib.settings.presentation.viewmodel

import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.settings.presentation.events.SettingsEvents
import io.github.juevigrace.diva.lib.settings.presentation.state.SettingsState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val sessionRepository: SessionRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        scope.launch { observeSettings() }
    }

    fun onEvent(event: SettingsEvents) {
        when (event) {
            SettingsEvents.OnBack -> onBack()
        }
    }

    private suspend fun observeSettings() {
        sessionRepository.getCurrent().fold(
            onSuccess = { session ->
                repository.observe(session.userId).collect { result ->
                    _state.value = SettingsState(settings = result)
                }
            },
            onFailure = { },
        )
    }

    private fun onBack() {
        navigator.pop()
    }
}
