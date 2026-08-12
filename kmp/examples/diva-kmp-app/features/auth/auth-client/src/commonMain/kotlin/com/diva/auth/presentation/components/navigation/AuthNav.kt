package com.diva.auth.presentation.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.diva.auth.presentation.forgot.ui.screen.ForgotScreen
import com.diva.auth.presentation.signIn.ui.screen.SignInScreen
import com.diva.auth.presentation.signUp.ui.screen.SignUpScreen
import com.diva.ui.navigation.Destination
import com.diva.ui.navigation.ForgotDestination
import com.diva.ui.navigation.SignInDestination
import com.diva.ui.navigation.SignUpDestination
import com.diva.ui.navigation.VerificationDestination
import com.diva.verification.presentation.ui.screen.VerificationScreen

fun EntryProviderScope<Destination>.authEntries() {
    entry<SignInDestination> {
        SignInScreen()
    }
    entry<SignUpDestination> {
        SignUpScreen()
    }
    entry<ForgotDestination> { destination ->
        ForgotScreen(destination.action)
    }
    entry<VerificationDestination> { destination ->
        VerificationScreen(destination.action)
    }
}
