package io.github.juevigrace.diva.lib.auth.presentation.viewmodel

import io.github.juevigrace.diva.lib.auth.domain.AuthRepository
import io.github.juevigrace.diva.lib.auth.presentation.events.SignInEvents
import io.github.juevigrace.diva.lib.auth.presentation.state.SignInState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SignInViewModel(
    private val repository: AuthRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<SignInState>
        field = MutableStateFlow(SignInState())

    fun onEvent(event: SignInEvents) {
        when (event) {
            SignInEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
