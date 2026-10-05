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
import kotlin.collections.plus
import kotlin.collections.take

interface TabNavigator : Navigator {
    val tabs: List<Tab>
    val tabBackStack: StateFlow<TabBackStack>

    fun selectTab(tab: Tab)
    fun popTab(): Boolean
    fun popTabUntil(tabRoute: Tab): Boolean
    fun clearTabHistory()

    companion object {
        fun create(tabs: List<Tab>, startTab: Tab = tabs.first()): TabNavigator =
            TabNavigatorImpl(tabs, startTab)
    }
}

/**
 * The tab-level counterpart of [BackStack]: a non-empty history whose last entry is the
 * selected tab, mirroring [BackStack.current] as [BackStack.entries]' last entry. [selectedTab]
 * is derived rather than stored so the two can never disagree.
 */
@Immutable
data class TabBackStack(
    val tabHistory: List<Tab>,
    /**
     * Entry stacks of the tabs that are not selected. The selected tab's live stack is
     * [Navigator.backStack]; [TabNavigator.selectTab] swaps between the two.
     */
    val savedStacks: Map<Tab, List<NavKey>>,
) {
    init {
        require(tabHistory.isNotEmpty()) { "TabBackStack must hold at least one entry" }
    }

    val selectedTab: Tab
        get() = tabHistory.last()

    val canPopTab: Boolean
        get() = tabHistory.size > 1
}

/**
 * Linear navigation inside the selected tab is inherited from [BaseNavigator], so [backStack]
 * always holds the selected tab's entries. [TabBackStack] carries the tab-level state, and
 * [TabNavigatorImpl.switchTo] is the only place the two are written together.
 */
@Stable
internal class TabNavigatorImpl(
    override val tabs: List<Tab>,
    startTab: Tab,
) : TabNavigator, BaseNavigator(startDestination = startTab.route) {

    /**
     * Each tab's root [NavKey] mapped back to its tab, so a flat list of entries can be
     * classified back into per-tab stacks.
     */
    private val tabRoots: Map<NavKey, Tab> = tabs.associateBy { it.route }

    override val tabBackStack: StateFlow<TabBackStack>
        field = MutableStateFlow(
            TabBackStack(
                tabHistory = listOf(startTab),
                savedStacks = emptyMap(),
            )
        )

    /**
     * Points the navigator at [history]'s last tab and swaps [backStack] over to that tab's
     * stack, saving the outgoing tab's stack so it comes back when the user returns to it.
     */
    private fun switchTo(history: List<Tab>) {
        val target = history.last()
        val outgoingStack = backStack.value.entries
        val incomingStack = tabBackStack.value.savedStacks[target] ?: listOf(target.route)
        tabBackStack.update { state ->
            state.copy(
                tabHistory = history,
                savedStacks = state.savedStacks + (state.selectedTab to outgoingStack) - target,
            )
        }
        setEntries(incomingStack)
    }

    override fun selectTab(tab: Tab) {
        val state = tabBackStack.value
        if (tab !in tabs || state.selectedTab == tab) return
        switchTo(state.tabHistory + tab)
    }

    override fun popTab(): Boolean {
        val state = tabBackStack.value
        if (state.tabHistory.size <= 1) return false
        switchTo(state.tabHistory.dropLast(1))
        return true
    }

    override fun popTabUntil(tabRoute: Tab): Boolean {
        val state = tabBackStack.value
        val index = state.tabHistory.lastIndexOf(tabRoute)
        if (index == -1) return false
        switchTo(state.tabHistory.take(index + 1))
        return true
    }

    override fun clearTabHistory() {
        tabBackStack.update { state ->
            state.copy(tabHistory = listOf(state.selectedTab))
        }
    }

    /**
     * Rebuilds tab state from a flat entry list, e.g. one restored from saved state.
     *
     * Each tab's root acts as the section delimiter, so entries are grouped into per-tab stacks
     * and a tab that never appears in [entries] still falls back to its own root rather than to
     * an empty stack. Roots are appended here only, never pre-seeded, or the restored active tab
     * would open with a duplicated root and a bogus [BackStack.canPop].
     */
    internal fun restoreFrom(entries: List<NavKey>) {
        val history = entries.mapNotNull { tabRoots[it] }
        if (history.isEmpty()) return

        val perTab = tabs.associateWith { mutableListOf<NavKey>() }.toMutableMap()
        var current: Tab? = null
        for (entry in entries) {
            val entryTab = tabRoots[entry]
            if (entryTab != null) {
                current = entryTab
                perTab.getValue(entryTab) += entry
            } else if (current != null) {
                perTab.getValue(current) += entry
            }
        }

        val last = history.last()
        val activeStack = perTab.getValue(last).ifEmpty { listOf(last.route) }
        val savedStacks = perTab
            .filterKeys { it != last }
            .mapValues { entry -> entry.value.toList().ifEmpty { listOf(entry.key.route) } }
        tabBackStack.update { state ->
            state.copy(
                tabHistory = history,
                savedStacks = savedStacks,
            )
        }
        setEntries(activeStack)
    }

    override fun navigate(destination: NavKey, launchSingleTop: Boolean) {
        require(destination !in tabRoots) {
            "Use selectTab() to switch tabs; got tab root $destination"
        }
        super.navigate(destination, launchSingleTop)
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
    val navigator = remember(tabs, startTab) { TabNavigatorImpl(tabs, startTab) }
    if (navBackStack != null) {
        LaunchedEffect(navigator) {
            snapshotFlow { navBackStack.toList() }
                .distinctUntilChanged()
                .collect { entries ->
                    navigator.restoreFrom(entries)
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
    val backStack: BackStack by tabNavigator.backStack.collectAsStateWithLifecycle()
    CompositionLocalProvider(LocalTabNavigator provides tabNavigator) {
        NavDisplay(
            modifier = modifier,
            backStack = backStack.entries,
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
