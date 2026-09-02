package io.github.juevigrace.diva.lib.ui.navigation

import androidx.navigation3.runtime.NavKey

sealed interface Screens : NavKey {

    sealed interface OnboardingGraph : Screens {
        data object Splash : OnboardingGraph
        data object Onboarding : OnboardingGraph
    }

    sealed interface AuthGraph : Screens {
        data object SignIn : AuthGraph
        data object SignUp : AuthGraph
        data object Forgot : AuthGraph
        data object Verification : AuthGraph
    }

    data object Account : Screens
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
