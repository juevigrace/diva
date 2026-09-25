package io.github.juevigrace.diva.lib.devices.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.lib.devices.presentation.ui.screen.DevicesScreen

fun EntryProviderScope<NavKey>.devicesNav() {
    entry<DevicesRoute> {
        DevicesScreen()
    }
}