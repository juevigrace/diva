package io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthGraph : NavKey {
    @Serializable
    data object SignIn : AuthGraph

    @Serializable
    data object SignUp : AuthGraph

    @Serializable
    data object Forgot : AuthGraph
}

typealias SignInRoute = AuthGraph.SignIn
typealias SignUpRoute = AuthGraph.SignUp
typealias ForgotRoute = AuthGraph.Forgot