package io.github.juevigrace.diva.lib.auth.presentation.viewmodel

import io.github.juevigrace.diva.lib.auth.domain.AuthRepository
import io.github.juevigrace.diva.lib.auth.presentation.events.ForgotEvents
import io.github.juevigrace.diva.lib.auth.presentation.state.ForgotState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForgotViewModel(
    private val repository: AuthRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<ForgotState>
        field = MutableStateFlow(ForgotState())

    fun onEvent(event: ForgotEvents) {
        when (event) {
            ForgotEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
