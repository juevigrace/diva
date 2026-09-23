package io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation.SettingsRoute
import io.github.juevigrace.diva.lib.settings.presentation.ui.screen.SettingsScreen

fun EntryProviderScope<NavKey>.settingsNav() {
    entry<SettingsRoute> {
        SettingsScreen()
    }
}
