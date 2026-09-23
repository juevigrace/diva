package io.github.juevigrace.diva.lib.verification.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.verification.presentation.ui.screen.VerificationScreen

fun EntryProviderScope<NavKey>.verificationNav() {
    entry<VerificationRoute> {
        VerificationScreen()
    }
}