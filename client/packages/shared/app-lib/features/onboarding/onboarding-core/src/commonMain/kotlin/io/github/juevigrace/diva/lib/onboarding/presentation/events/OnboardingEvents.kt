package io.github.juevigrace.diva.lib.onboarding.presentation.events

sealed interface OnboardingEvents {
    data object OnBack : OnboardingEvents
}
