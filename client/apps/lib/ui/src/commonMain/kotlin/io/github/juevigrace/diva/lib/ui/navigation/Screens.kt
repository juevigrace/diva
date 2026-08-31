package io.github.juevigrace.diva.lib.ui.navigation

import androidx.navigation3.runtime.NavKey

sealed interface Screens : NavKey {
    data object Splash : Screens
}
