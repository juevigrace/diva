package io.github.juevigrace.diva.lib.settings.presentation.viewmodel

import io.github.juevigrace.diva.lib.settings.domain.SharedSettingsRepository
import io.github.juevigrace.diva.lib.settings.presentation.state.SharedSettingsState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class SharedSettingsViewModel<R : SharedSettingsRepository, S : SharedSettingsState>(
    protected open val repository: R,
    protected open val navigator: Navigator,
) : DivaViewModel() {
    abstract val state: StateFlow<S>

    protected fun onBack() {
        navigator.pop()
    }
}
