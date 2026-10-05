/*
 * Reference: the alternative tab navigator shape. NOT COMPILED -- every declaration below is
 * commented out, because the type names collide with TabNavigator.kt.
 *
 * WHY THIS FILE EXISTS
 *
 * TabNavigator.kt merges the tab state and the linear navigation state into two flows that are
 * written together by `TabNavigatorImpl.switchTo`. That keeps `BaseNavigator` (and its five
 * linear operations) reusable, at the cost of writing `tabBackStack` and `backStack` one after
 * the other inside `switchTo`.
 *
 * The shape below takes the opposite trade: `TabBackStack` is the single writer and
 * `Navigator.backStack` is a projection of it, so the two flows can never disagree.
 *
 * THE PROJECTION
 *
 *     private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
 *
 *     override val backStack: StateFlow<BackStack> = tabBackStack
 *         .map { BackStack(it.selectedTab.route, it.currentBackStack) }
 *         .stateIn(scope, SharingStarted.Eagerly, BackStack(startTab.route, listOf(startTab.route)))
 *
 * `map` is the usual cold Flow operator and does nothing until collected; `stateIn` upgrades it to
 * a hot, shared, always-current StateFlow. `SharingStarted.Eagerly` starts collecting immediately
 * rather than on first subscriber, and `initial` is mandatory because a StateFlow needs a value
 * before the first emission lands.
 *
 * Two things to get right if this is ever adopted:
 *
 *   - Use an unconfined/immediate dispatcher. With `ioDispatcher` the projection updates on a
 *     background thread, so `backStack` trails `tabBackStack` by a dispatch hop on every
 *     navigation -- worst on wasm/js, where `Dispatchers.Default` is the main thread anyway.
 *   - Declaration order matters. `stateIn` on an unconfined dispatcher collects *synchronously*
 *     during the initializer, so `backStack` must be declared after `tabBackStack`, and `initial`
 *     must be built from the constructor params rather than read off `tabBackStack`.
 *
 * THE COST: BaseNavigator cannot be inherited here. It owns its own MutableStateFlow and all five
 * of its operations write to it, so extending it would give the navigator a second flow that
 * nothing renders from -- `navigate()` would update a stack nobody reads. Every linear operation
 * has to be re-implemented against the map, which is the code BaseNavigator was extracted to
 * deduplicate in the first place.
 *
 * THE ONE REAL ADVANTAGE: tab switching needs no stack swap at all, so `selectTab`, `popTab` and
 * `popTabUntil` stay trivial -- they only touch `tabBackStack` and the projection follows.
 */

package io.github.juevigrace.diva.ui.navigation

// ---------------------------------------------------------------------------------------------
// TabBackStack: the map is authoritative again, so `currentBackStack` and `canPop` come back.
// `selectedTab` stays derived from `tabHistory.last()`, same as in TabNavigator.kt.
// ---------------------------------------------------------------------------------------------
//
// @Immutable
// data class TabBackStack(
//     val tabs: Map<Tab, List<NavKey>>,
//     val tabHistory: List<Tab>,
// ) {
//     init {
//         require(tabHistory.isNotEmpty()) { "TabBackStack must hold at least one entry" }
//     }
//
//     val selectedTab: Tab
//         get() = tabHistory.last()
//
//     val currentBackStack: List<NavKey>
//         get() = tabs[selectedTab].orEmpty()
//
//     val canPop: Boolean
//         get() = currentBackStack.size > 1
//
//     val canPopTab: Boolean
//         get() = tabHistory.size > 1
// }

// ---------------------------------------------------------------------------------------------
// TabNavigator: unchanged from TabNavigator.kt except for popTabUntil returning Boolean.
// ---------------------------------------------------------------------------------------------
//
// interface TabNavigator : Navigator {
//     val tabs: List<Tab>
//     val tabBackStack: StateFlow<TabBackStack>
//
//     fun selectTab(tab: Tab)
//     fun popTab(): Boolean
//     fun popTabUntil(tabRoute: Tab): Boolean
//     fun clearTabHistory()
//
//     companion object {
//         fun create(tabs: List<Tab>, startTab: Tab = tabs.first()): TabNavigator =
//             TabNavigatorImpl(tabs, startTab)
//     }
// }

// ---------------------------------------------------------------------------------------------
// TabNavigatorImpl: no BaseNavigator. One scope, one writer, five re-implemented operations.
// ---------------------------------------------------------------------------------------------
//
// @Stable
// internal class TabNavigatorImpl(
//     override val tabs: List<Tab>,
//     startTab: Tab,
// ) : TabNavigator {
//
//     /**
//      * Each tab's root [NavKey] mapped back to its tab, so a flat list of entries can be
//      * classified back into per-tab stacks.
//      */
//     private val tabRoots: Map<NavKey, Tab> = tabs.associateBy { it.route }
//
//     /**
//      * Never cancelled. This is a process-lifetime Koin singleton, so an uncancelled collector
//      * retains nothing that was not already retained; `Dispatchers.Unconfined` keeps the
//      * projection below synchronous with the writes that drive it.
//      */
//     private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Unconfined)
//
//     override val tabBackStack: StateFlow<TabBackStack> = MutableStateFlow(
//         TabBackStack(
//             tabs = tabs.associate { it to listOf(it.route) },
//             tabHistory = listOf(startTab),
//         )
//     )
//
//     // MUST stay declared after tabBackStack: stateIn on an unconfined dispatcher begins
//     // collecting synchronously, so tabBackStack has to be initialised by this point.
//     override val backStack: StateFlow<BackStack> = tabBackStack
//         .map { BackStack(it.selectedTab.route, it.currentBackStack) }
//         .stateIn(scope, SharingStarted.Eagerly, BackStack(startTab.route, listOf(startTab.route)))
//
//     // --- linear operations, re-implemented against the map ---
//
//     override fun navigate(destination: NavKey, launchSingleTop: Boolean) {
//         require(destination !in tabRoots) {
//             "Use selectTab() to switch tabs; got tab root $destination"
//         }
//         tabBackStack.update { state ->
//             val current = state.tabs[state.selectedTab].orEmpty()
//             if (launchSingleTop && current.lastOrNull() == destination) return@update state
//             state.copy(tabs = state.tabs + (state.selectedTab to (current + destination)))
//         }
//     }
//
//     override fun pop(): Boolean {
//         var popped = false
//         tabBackStack.update { state ->
//             val current = state.tabs[state.selectedTab].orEmpty()
//             if (current.size <= 1) return@update state
//             popped = true
//             state.copy(tabs = state.tabs + (state.selectedTab to current.dropLast(1)))
//         }
//         return popped
//     }
//
//     override fun popUntil(destination: NavKey) {
//         tabBackStack.update { state ->
//             val current = state.tabs[state.selectedTab].orEmpty()
//             val index = current.lastIndexOf(destination)
//             if (index == -1) return@update state
//             state.copy(tabs = state.tabs + (state.selectedTab to current.take(index + 1)))
//         }
//     }
//
//     override fun replaceTop(destination: NavKey) {
//         tabBackStack.update { state ->
//             val current = state.tabs[state.selectedTab].orEmpty()
//             if (current.isEmpty() || current.last() == destination) return@update state
//             state.copy(tabs = state.tabs + (state.selectedTab to current.dropLast(1) + destination))
//         }
//     }
//
//     override fun replaceAll(destination: NavKey) {
//         tabBackStack.update { state ->
//             state.copy(tabs = state.tabs + (state.selectedTab to listOf(destination)))
//         }
//     }
//
//     // --- tab operations: no stack swap, because the projection follows selectedTab ---
//
//     override fun selectTab(tab: Tab) {
//         if (tab !in tabs) return
//         tabBackStack.update { state ->
//             if (state.selectedTab == tab) return@update state
//             state.copy(
//                 tabs = state.tabs + (tab to (state.tabs[tab] ?: listOf(tab.route))),
//                 tabHistory = state.tabHistory + tab,
//             )
//         }
//     }
//
//     override fun popTab(): Boolean {
//         var popped = false
//         tabBackStack.update { state ->
//             if (state.tabHistory.size <= 1) return@update state
//             popped = true
//             val newHistory = state.tabHistory.dropLast(1)
//             state.copy(tabHistory = newHistory)
//         }
//         return popped
//     }
//
//     override fun popTabUntil(tabRoute: Tab): Boolean {
//         var popped = false
//         tabBackStack.update { state ->
//             val index = state.tabHistory.lastIndexOf(tabRoute)
//             if (index == -1) return@update state
//             popped = true
//             val newHistory = state.tabHistory.take(index + 1)
//             state.copy(tabHistory = newHistory)
//         }
//         return popped
//     }
//
//     override fun clearTabHistory() {
//         tabBackStack.update { state ->
//             state.copy(tabHistory = listOf(state.selectedTab))
//         }
//     }
//
//     /**
//      * Rebuilds tab state from a flat entry list, e.g. one restored from saved state.
//      *
//      * Each tab's root acts as the section delimiter, so entries are grouped into per-tab stacks
//      * and a tab that never appears in [entries] still falls back to its own root. Roots are
//      * appended here only, never pre-seeded, or the restored active tab would open with a
//      * duplicated root and a bogus canPop.
//      */
//     internal fun restoreFrom(entries: List<NavKey>) {
//         val history = entries.mapNotNull { tabRoots[it] }
//         if (history.isEmpty()) return
//
//         val perTab = tabs.associate { it to mutableListOf<NavKey>() }.toMutableMap()
//         var current: Tab? = null
//         for (entry in entries) {
//             val entryTab = tabRoots[entry]
//             if (entryTab != null) {
//                 current = entryTab
//                 perTab.getValue(entryTab) += entry
//             } else if (current != null) {
//                 perTab.getValue(current) += entry
//             }
//         }
//
//         val last = history.last()
//         val restored = perTab.mapValues { entry -> entry.value.toList().ifEmpty { listOf(entry.key.route) } }
//         tabBackStack.update { state ->
//             state.copy(
//                 tabHistory = history,
//                 tabs = restored,
//             )
//         }
//     }
// }
//
// // rememberTabNavigator and TabNavHost are identical to TabNavigator.kt in this shape: the outer
// // NavBackStack is still NavBackStack<NavKey> (rememberNavBackStack is not generic in its return
// // type), and TabNavHost still renders `tabNavigator.backStack.entries`.

// ---------------------------------------------------------------------------------------------
// Extra imports this shape would need beyond TabNavigator.kt:
// ---------------------------------------------------------------------------------------------
//
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.SupervisorJob
// import kotlinx.coroutines.flow.SharingStarted
// import kotlinx.coroutines.flow.map
// import kotlinx.coroutines.flow.stateIn