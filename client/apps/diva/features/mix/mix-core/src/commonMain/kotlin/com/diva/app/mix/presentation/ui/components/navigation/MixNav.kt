package com.diva.app.mix.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.mix.presentation.ui.screen.MixScreen

fun EntryProviderScope<NavKey>.mixNav() {
    entry<MixRoute> {
        MixScreen()
    }
}