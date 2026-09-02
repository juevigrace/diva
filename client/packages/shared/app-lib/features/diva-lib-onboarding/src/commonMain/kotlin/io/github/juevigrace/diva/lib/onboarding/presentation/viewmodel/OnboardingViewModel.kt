package io.github.juevigrace.diva.lib.onboarding.presentation.viewmodel

import io.github.juevigrace.diva.lib.onboarding.domain.OnboardingRepository
import io.github.juevigrace.diva.lib.onboarding.presentation.events.OnboardingEvents
import io.github.juevigrace.diva.lib.onboarding.presentation.state.OnboardingState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OnboardingViewModel(
    private val repository: OnboardingRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<OnboardingState>
        field = MutableStateFlow(OnboardingState())

    fun onEvent(event: OnboardingEvents) {
        when (event) {
            OnboardingEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}
