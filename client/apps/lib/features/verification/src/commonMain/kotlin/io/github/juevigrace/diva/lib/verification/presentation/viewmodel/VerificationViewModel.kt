package io.github.juevigrace.diva.lib.verification.presentation.viewmodel

import io.github.juevigrace.diva.lib.verification.domain.VerificationRepository
import io.github.juevigrace.diva.lib.verification.presentation.events.VerificationEvents
import io.github.juevigrace.diva.lib.verification.presentation.state.VerificationState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

class VerificationViewModel(
    private val repository: VerificationRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<VerificationState>
        field = MutableStateFlow(VerificationState())

    fun onEvent(event: VerificationEvents) {
        when (event) {
            VerificationEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
