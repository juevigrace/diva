package io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.auth.presentation.ui.screen.ForgotScreen
import io.github.juevigrace.diva.lib.auth.presentation.ui.screen.SignInScreen
import io.github.juevigrace.diva.lib.auth.presentation.ui.screen.SignUpScreen
import io.github.juevigrace.diva.lib.ui.navigation.ForgotRoute
import io.github.juevigrace.diva.lib.ui.navigation.SignInRoute
import io.github.juevigrace.diva.lib.ui.navigation.SignUpRoute

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
