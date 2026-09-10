package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Stable
internal class DefaultTabNavigator(
    tabs: List<Tab>,
    startTab: Tab,
) : TabNavigator {

    override val tabs: List<Tab> = tabs

    private val tabRoutes: Set<NavKey> = tabs.map { it.route }.toSet()

    override val backStack: StateFlow<TabBackStack>
        field = MutableStateFlow(
            TabBackStack(
                tabs = tabs.associate { it.route to listOf(it.route) },
                selectedTab = startTab.route,
                tabHistory = listOf(startTab.route),
            )
        )

    internal fun syncFromBackStack(entries: List<NavKey>) {
        backStack.update { state ->
            val perTab = mutableMapOf<NavKey, MutableList<NavKey>>()
            var currentTab: NavKey? = null

            for (entry in entries) {
                if (entry in tabRoutes) {
                    currentTab = entry
                    perTab.getOrPut(entry) { mutableListOf() }.add(entry)
                } else if (currentTab != null) {
                    perTab[currentTab]!!.add(entry)
                }
            }

            val lastTab = entries.lastOrNull { it in tabRoutes } ?: state.selectedTab
            state.copy(
                tabs = perTab.mapValues { it.value.toList() },
                selectedTab = lastTab,
                tabHistory = entries.filter { it in tabRoutes },
            )
        }
    }

    override fun selectTab(tab: Tab) {
        backStack.update { state ->
            if (state.selectedTab == tab.route) return@update state
            val updatedTabs = if (tab.route !in state.tabs) {
                state.tabs + (tab.route to listOf(tab.route))
            } else {
                state.tabs
            }
            state.copy(
                tabs = updatedTabs,
                selectedTab = tab.route,
                tabHistory = state.tabHistory + tab.route,
            )
        }
    }

    override fun navigate(destination: NavKey) {
        backStack.update { state ->
            val currentEntries = state.tabs[state.selectedTab].orEmpty()
            if (currentEntries.lastOrNull() == destination) return@update state
            state.copy(
                tabs = state.tabs + (state.selectedTab to (currentEntries + destination)),
            )
        }
    }

    override fun pop(): Boolean {
        var popped = false
        backStack.update { state ->
            val currentEntries = state.tabs[state.selectedTab].orEmpty()
            if (currentEntries.size <= 1) return@update state
            popped = true
            state.copy(
                tabs = state.tabs + (state.selectedTab to currentEntries.dropLast(1)),
            )
        }
        return popped
    }

    override fun popUntil(destination: NavKey) {
        backStack.update { state ->
            val currentEntries = state.tabs[state.selectedTab].orEmpty()
            val index = currentEntries.lastIndexOf(destination)
            if (index == -1) return@update state
            state.copy(
                tabs = state.tabs + (state.selectedTab to currentEntries.take(index + 1)),
            )
        }
    }

    override fun replaceTop(destination: NavKey) {
        backStack.update { state ->
            val currentEntries = state.tabs[state.selectedTab].orEmpty()
            if (currentEntries.isEmpty() || currentEntries.last() == destination) return@update state
            state.copy(
                tabs = state.tabs + (state.selectedTab to currentEntries.dropLast(1) + destination),
            )
        }
    }

    override fun replaceAll(destination: NavKey) {
        backStack.update { state ->
            state.copy(
                tabs = state.tabs + (state.selectedTab to listOf(destination)),
            )
        }
    }
}
