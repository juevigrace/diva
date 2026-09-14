package io.github.juevigrace.diva.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlin.collections.dropLast
import kotlin.collections.last
import kotlin.collections.lastOrNull
import kotlin.collections.orEmpty
import kotlin.collections.plus
import kotlin.collections.take

@Immutable
data class TabBackStack(
    val tabs: Map<NavKey, List<NavKey>>,
    val selectedTab: NavKey,
    val tabHistory: List<NavKey>,
) {
    val currentBackStack: List<NavKey>
        get() = tabs[selectedTab].orEmpty()

    val canPop: Boolean
        get() = currentBackStack.size > 1

    val canPopTab: Boolean
        get() = tabHistory.size > 1
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

    fun popTab(): Boolean
    fun popTabUntil(tabRoute: NavKey)
    fun clearTabHistory()

    companion object {
        fun create(tabs: List<Tab>, startTab: Tab = tabs.first()): TabNavigator =
            TabNavigatorImpl(tabs, startTab)
    }
}

@Stable
internal class TabNavigatorImpl(override val tabs: List<Tab>, startTab: Tab) : TabNavigator {
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

    override fun popTab(): Boolean {
        var popped = false
        backStack.update { state ->
            if (state.tabHistory.size <= 1) return@update state
            popped = true
            val newHistory = state.tabHistory.dropLast(1)
            state.copy(
                tabHistory = newHistory,
                selectedTab = newHistory.last(),
            )
        }
        return popped
    }

    override fun popTabUntil(tabRoute: NavKey) {
        backStack.update { state ->
            val index = state.tabHistory.lastIndexOf(tabRoute)
            if (index == -1) return@update state
            val newHistory = state.tabHistory.take(index + 1)
            state.copy(
                tabHistory = newHistory,
                selectedTab = newHistory.last(),
            )
        }
    }

    override fun clearTabHistory() {
        backStack.update { state ->
            state.copy(
                tabHistory = listOf(state.selectedTab),
            )
        }
    }
}


val LocalTabNavigator = staticCompositionLocalOf<TabNavigator> { error("No TabNavigator provided") }

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
    val navigator = remember { TabNavigatorImpl(tabs, startTab) }
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

@Composable
fun TabNavHost(
    tabNavigator: TabNavigator,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onBack: () -> Unit = { tabNavigator.pop() },
    entryDecorators: List<NavEntryDecorator<NavKey>> = listOf(rememberSaveableStateHolderNavEntryDecorator()),
    sceneStrategies: List<SceneStrategy<NavKey>> = listOf(SinglePaneSceneStrategy()),
    sizeTransform: SizeTransform? = null,
    transitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        defaultTransitionSpec(),
    popTransitionSpec: AnimatedContentTransitionScope<Scene<NavKey>>.() -> ContentTransform =
        defaultPopTransitionSpec(),
    predictivePopTransitionSpec:
    AnimatedContentTransitionScope<Scene<NavKey>>.(
        @NavigationEvent.SwipeEdge Int
    ) -> ContentTransform =
        defaultPredictivePopTransitionSpec(),
    entryProvider: (key: NavKey) -> NavEntry<NavKey>,
) {
    val backStack: TabBackStack by tabNavigator.backStack.collectAsStateWithLifecycle()
    CompositionLocalProvider(LocalTabNavigator provides tabNavigator) {
        NavDisplay(
            modifier = modifier,
            backStack = backStack.currentBackStack,
            contentAlignment = contentAlignment,
            onBack = onBack,
            entryDecorators = entryDecorators,
            sceneStrategies = sceneStrategies,
            sizeTransform = sizeTransform,
            transitionSpec = transitionSpec,
            popTransitionSpec = popTransitionSpec,
            predictivePopTransitionSpec = predictivePopTransitionSpec,
            entryProvider = entryProvider,
        )
    }
}