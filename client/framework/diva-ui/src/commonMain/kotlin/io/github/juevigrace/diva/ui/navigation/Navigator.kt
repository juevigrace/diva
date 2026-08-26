package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Immutable
data class BackStack(
    val startDestination: NavKey,
    val entries: List<NavKey>,
) {
    val current: Option<NavKey>
        get() = Option.of(entries.lastOrNull())
}

interface Navigator {
    val backStack: StateFlow<BackStack>

    fun navigate(destination: NavKey, launchSingleTop: Boolean = true)
    fun pop(): Boolean
    fun popUntil(destination: NavKey)
    fun replaceTop(destination: NavKey)
    fun replaceAll(destination: NavKey)
}

@Composable
fun rememberNavigator(
    startDestination: NavKey,
    configuration: SavedStateConfiguration? = LocalSavedStateConfiguration.current,
): Navigator {
    val navBackStack: NavBackStack<NavKey>? = if (configuration != null) {
        rememberNavBackStack(configuration, startDestination)
    } else {
        null
    }
    val navigator = remember { DefaultNavigator(startDestination) }
    if (navBackStack != null) {
        LaunchedEffect(navBackStack) {
            snapshotFlow { navBackStack.toList() }
                .distinctUntilChanged()
                .collect { entries ->
                    navigator.syncFromBackStack(entries)
                }
        }
    }
    return navigator
}
