package com.diva.app.profile.presentation.ui.components.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.diva.app.profile.presentation.ui.screen.ProfileScreen

fun EntryProviderScope<NavKey>.profileNav() {
    entry<ProfileRoute> {
        ProfileScreen()
    }
}