package io.github.juevigrace.diva.lib.permissions.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.permissions.presentation.ui.screen.PermissionsScreen

fun EntryProviderScope<NavKey>.permissionsNav() {
    entry<PermissionsRoute> {
        PermissionsScreen()
    }
}