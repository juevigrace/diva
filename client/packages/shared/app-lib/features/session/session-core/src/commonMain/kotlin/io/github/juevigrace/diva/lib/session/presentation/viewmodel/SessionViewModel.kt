package io.github.juevigrace.diva.lib.session.presentation.viewmodel

import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.presentation.events.SessionEvents
import io.github.juevigrace.diva.lib.session.presentation.state.SessionState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SessionViewModel(
    private val repository: SessionRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<SessionState>
        field = MutableStateFlow(SessionState())

    fun onEvent(event: SessionEvents) {
        when (event) {
            SessionEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
