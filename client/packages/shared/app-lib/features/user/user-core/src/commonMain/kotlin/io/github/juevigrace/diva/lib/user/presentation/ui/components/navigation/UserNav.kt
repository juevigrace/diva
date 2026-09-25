package io.github.juevigrace.diva.lib.user.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.user.presentation.ui.screen.UserScreen

fun EntryProviderScope<NavKey>.userNav() {
    entry<AccountRoute> {
        UserScreen()
    }
}