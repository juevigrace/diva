package io.github.juevigrace.diva.lib.onboarding.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface OnboardingGraph : NavKey {
    @Serializable
    data object Splash : OnboardingGraph

    @Serializable
    data object Onboarding : OnboardingGraph
}

typealias SplashRoute = OnboardingGraph.Splash
typealias OnboardingRoute = OnboardingGraph.Onboarding