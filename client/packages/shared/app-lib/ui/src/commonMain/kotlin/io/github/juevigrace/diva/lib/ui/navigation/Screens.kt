package io.github.juevigrace.diva.lib.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// TODO: maybe move each graph to their corresponding module will add more flexibility at creating routes?
@Serializable
sealed interface Screens : NavKey {

    @Serializable
    sealed interface OnboardingGraph : Screens {
        @Serializable
        data object Splash : OnboardingGraph

        @Serializable
        data object Onboarding : OnboardingGraph
    }

    @Serializable
    sealed interface AuthGraph : Screens {
        @Serializable
        data object SignIn : AuthGraph

        @Serializable
        data object SignUp : AuthGraph

        @Serializable
        data object Forgot : AuthGraph

        @Serializable
        data object Verification : AuthGraph
    }

    @Serializable
    data object Account : Screens

    @Serializable
    data object Settings : Screens
}

typealias SplashRoute = Screens.OnboardingGraph.Splash
typealias OnboardingRoute = Screens.OnboardingGraph.Onboarding
typealias SignInRoute = Screens.AuthGraph.SignIn
typealias SignUpRoute = Screens.AuthGraph.SignUp
typealias ForgotRoute = Screens.AuthGraph.Forgot
typealias VerificationRoute = Screens.AuthGraph.Verification
typealias AccountRoute = Screens.Account
typealias SettingsRoute = Screens.Settings
