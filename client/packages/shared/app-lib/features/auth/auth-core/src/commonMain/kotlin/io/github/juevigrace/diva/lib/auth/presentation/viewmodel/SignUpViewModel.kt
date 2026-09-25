package io.github.juevigrace.diva.lib.auth.presentation.viewmodel

import io.github.juevigrace.diva.lib.auth.domain.AuthRepository
import io.github.juevigrace.diva.lib.auth.presentation.events.SignUpEvents
import io.github.juevigrace.diva.lib.auth.presentation.state.SignUpState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignUpViewModel(
    private val repository: AuthRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<SignUpState>
        field = MutableStateFlow(SignUpState())

    fun onEvent(event: SignUpEvents) {
        when (event) {
            SignUpEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
