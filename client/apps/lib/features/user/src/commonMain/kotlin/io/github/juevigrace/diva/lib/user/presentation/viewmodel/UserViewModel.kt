package io.github.juevigrace.diva.lib.user.presentation.viewmodel

import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.presentation.events.UserEvents
import io.github.juevigrace.diva.lib.user.presentation.state.UserState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel(
    private val repository: UserRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<UserState>
        field = MutableStateFlow(UserState())

    fun onEvent(event: UserEvents) {
        when (event) {
            UserEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
