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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Immutable
data class TabBackStack(
    val tabs: Map<NavKey, List<NavKey>>,
    val selectedTab: NavKey,
    val tabHistory: List<NavKey>,
) {
    val currentBackStack: List<NavKey>
        get() = tabs[selectedTab].orEmpty()
}

interface TabNavigator {
    val tabs: List<Tab>
    val backStack: StateFlow<TabBackStack>

    fun selectTab(tab: Tab)
    fun navigate(destination: NavKey)
    fun pop(): Boolean
    fun popUntil(destination: NavKey)
    fun replaceTop(destination: NavKey)
    fun replaceAll(destination: NavKey)

    companion object {
        fun create(tabs: List<Tab>, startTab: Tab = tabs.first()): TabNavigator =
            DefaultTabNavigator(tabs, startTab)
    }
}

@Composable
fun rememberTabNavigator(
    tabs: List<Tab>,
    startTab: Tab = tabs.first(),
    configuration: SavedStateConfiguration? = LocalSavedStateConfiguration.current,
): TabNavigator {
    val navBackStack: NavBackStack<NavKey>? = if (configuration != null) {
        rememberNavBackStack(configuration, startTab.route)
    } else {
        null
    }
    val navigator = remember { DefaultTabNavigator(tabs, startTab) }
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
