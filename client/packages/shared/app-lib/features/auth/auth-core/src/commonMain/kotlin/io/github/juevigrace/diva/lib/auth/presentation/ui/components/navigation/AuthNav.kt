package io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation.ForgotRoute
import io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation.SignInRoute
import io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation.SignUpRoute
import io.github.juevigrace.diva.lib.auth.presentation.ui.screen.ForgotScreen
import io.github.juevigrace.diva.lib.auth.presentation.ui.screen.SignInScreen
import io.github.juevigrace.diva.lib.auth.presentation.ui.screen.SignUpScreen

fun EntryProviderScope<NavKey>.authNav() {
    entry<SignInRoute> {
        SignInScreen()
    }
    entry<SignUpRoute> {
        SignUpScreen()
    }
    entry<ForgotRoute> {
        ForgotScreen()
    }
}
