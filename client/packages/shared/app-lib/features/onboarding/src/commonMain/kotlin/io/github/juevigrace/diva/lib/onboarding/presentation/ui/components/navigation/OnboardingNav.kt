package io.github.juevigrace.diva.lib.onboarding.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.onboarding.presentation.ui.screen.OnboardingScreen
import io.github.juevigrace.diva.lib.onboarding.presentation.ui.screen.SplashScreen

fun EntryProviderScope<NavKey>.onboardingNav() {
    entry<SplashRoute> {
        SplashScreen()
    }
    entry<OnboardingRoute> {
        OnboardingScreen()
    }
}