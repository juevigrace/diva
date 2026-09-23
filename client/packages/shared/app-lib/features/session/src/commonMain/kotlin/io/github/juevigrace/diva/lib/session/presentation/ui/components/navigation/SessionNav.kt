package io.github.juevigrace.diva.lib.session.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.session.presentation.ui.screen.SessionScreen

fun EntryProviderScope<NavKey>.sessionNav() {
    entry<SessionRoute> {
        SessionScreen()
    }
}