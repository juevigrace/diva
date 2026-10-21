package com.diva.app.home.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavKey
import com.diva.app.home.presentation.events.HomeEvents
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.search.presentation.ui.components.navigation.SearchResultsRoute
import com.diva.app.search.presentation.ui.components.navigation.SearchRoute
import com.diva.app.settings.domain.SettingsRepository
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.models.SessionStatus
import io.github.juevigrace.diva.lib.session.models.SessionType
import io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation.SettingsRoute
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.ui.navigation.Tab
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * One undoable step of the desktop forward arrow, recorded whenever the tab-level or
 * linear stack shrinks. The type remembers which stack the pop came from so [onForward]
 * can replay it through the matching API.
 */
private sealed interface ForwardStep {
    /** A popped linear entry, replayed with [TabNavigator.navigate]. */
    data class Entry(val key: NavKey) : ForwardStep
    /** A popped tab, replayed with [TabNavigator.selectTab]. */
    data class TabSwitch(val tab: Tab) : ForwardStep
}

/** A point-in-time frame of both navigation stacks, the unit the diff works on. */
private data class Snapshot(
    val tabHistory: List<Tab>,
    val entries: List<NavKey>,
)

class HomeViewModel(
    private val setRepo: SettingsRepository,
    private val userRepo: UserRepository,
    private val sessionRepo: SessionRepository,
    private val tabNavigator: TabNavigator,
) : DivaViewModel() {
    val state: StateFlow<HomeState>
        field = MutableStateFlow(HomeState())

    /**
     * Redo steps for the desktop arrows, most recent last. Pops are not routed through
     * this view model (the app-level BackHandler and NavDisplay pop on their own), so
     * steps are derived by diffing the two stack flows instead: any shrink is undo,
     * any growth that is not this view model's own forward replay is new navigation
     * and invalidates the redo tail, browser-style.
     */
    private val forwardSteps = mutableListOf<ForwardStep>()

    private var last by mutableStateOf(
        Snapshot(
            tabHistory = tabNavigator.tabBackStack.value.tabHistory,
            entries = tabNavigator.backStack.value.entries,
        )
    )

    /**
     * The snapshot a forward replay is expected to produce. The collector uses it to
     * tell the view model's own growth apart from user navigation; without it every
     * replay would look like fresh navigation and clear the remaining steps.
     */
    private var expected: Snapshot? = null

    init {
        scope.launch {
            launch {
                setRepo.observe().collect { result ->
                    result.map { settings ->
                        state.update { state ->
                            state.copy(settings = settings)
                        }
                    }
                }
            }
            launch {
                userRepo.observeCurrent().collect { result ->
                    result.map { user ->
                        state.update { state ->
                            state.copy(user = user)
                        }
                    }
                }
            }
            launch {
                combine(
                    sessionRepo.getSessions(),
                    userRepo.observe(),
                ) { sessions, users ->
                    val sessionUserIds = sessions
                        .getOrDefault(emptyList())
                        .filter {
                            it.type == SessionType.NORMAL && it.status == SessionStatus.ACTIVE
                        }
                        .map { it.userId }
                        .toSet()
                    users.getOrDefault(emptyList()).filter { it.id in sessionUserIds }
                }.collect { accounts ->
                    state.update { state ->
                        state.copy(accounts = accounts)
                    }
                }
            }
            launch {
                combine(
                    tabNavigator.tabBackStack,
                    tabNavigator.backStack,
                ) { tabBackStack, backStack ->
                    Snapshot(
                        tabHistory = tabBackStack.tabHistory,
                        entries = backStack.entries,
                    )
                }.collect { snapshot -> onStacksChanged(snapshot) }
            }
        }
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.OnBack -> onBack()
            HomeEvents.OnForward -> onForward()
            HomeEvents.OnOpenSearch -> tabNavigator.selectTab(SearchRoute)
            HomeEvents.OnOpenSearchResults -> tabNavigator.navigate(SearchResultsRoute)
            HomeEvents.OnProfileClick -> state.update { it.copy(showProfileDialog = true) }
            HomeEvents.OnDismissProfile -> state.update {
                it.copy(
                    showProfileDialog = false,
                    accountsExpanded = false,
                    selectedAccountId = null,
                )
            }
            HomeEvents.OnToggleAccounts -> state.update {
                it.copy(accountsExpanded = !it.accountsExpanded)
            }
            // TODO: switch the active session to the selected account's
            is HomeEvents.OnSelectAccount -> state.update {
                it.copy(selectedAccountId = event.user.id)
            }
            // TODO: add account flow
            HomeEvents.OnAddAccount -> Unit
            HomeEvents.OnOpenProfile -> {
                tabNavigator.navigate(ProfileRoute)
                onEvent(HomeEvents.OnDismissProfile)
            }
            HomeEvents.OnOpenSettings -> {
                tabNavigator.navigate(SettingsRoute)
                onEvent(HomeEvents.OnDismissProfile)
            }
            // TODO: sign out flow
            HomeEvents.OnSignOut -> Unit
        }
    }

    /**
     * Linear back across both stacks: pop the selected tab's inner stack first, and
     * only leave the tab when that stack is back at its root.
     */
    private fun onBack() {
        if (!tabNavigator.pop()) {
            tabNavigator.popTab()
        }
    }

    /**
     * Replays the most recent [ForwardStep] through the same public API the forward
     * navigation originally used, so the forward arrow is the exact inverse of the
     * back walk: entries are re-pushed with a non-singleTop [TabNavigator.navigate]
     * and tabs are re-selected, which restores their parked inner stack.
     */
    private fun onForward() {
        val step = forwardSteps.removeLastOrNull() ?: return
        val before = Snapshot(
            tabHistory = tabNavigator.tabBackStack.value.tabHistory,
            entries = tabNavigator.backStack.value.entries,
        )
        expected = when (step) {
            is ForwardStep.Entry -> {
                tabNavigator.navigate(step.key, launchSingleTop = false)
                before.copy(entries = before.entries + step.key)
            }
            is ForwardStep.TabSwitch -> {
                val restored = tabNavigator.tabBackStack.value.savedStacks[step.tab]
                    ?: listOf(step.tab.route)
                tabNavigator.selectTab(step.tab)
                Snapshot(
                    tabHistory = before.tabHistory + step.tab,
                    entries = restored,
                )
            }
        }
        publishFlags()
    }

    private fun onStacksChanged(snapshot: Snapshot) {
        val previous = last
        last = snapshot
        if (snapshot == previous) return

        val replayed = expected
        if (replayed != null) {
            // The view model's own forward replay: keep the remaining steps.
            if (snapshot == replayed) expected = null
            publishFlags()
            return
        }

        val historyShrunk = snapshot.tabHistory.size < previous.tabHistory.size &&
            previous.tabHistory.subList(0, snapshot.tabHistory.size) == snapshot.tabHistory
        val entriesShrunk = snapshot.entries.size < previous.entries.size &&
            snapshot.tabHistory == previous.tabHistory &&
            previous.entries.subList(0, snapshot.entries.size) == snapshot.entries

        when {
            historyShrunk -> previous.tabHistory.drop(snapshot.tabHistory.size)
                .forEach { forwardSteps += ForwardStep.TabSwitch(it) }
            entriesShrunk -> previous.entries.drop(snapshot.entries.size)
                .forEach { forwardSteps += ForwardStep.Entry(it) }
            else -> forwardSteps.clear()
        }
        publishFlags()
    }

    private fun publishFlags() {
        state.update { state ->
            state.copy(
                canGoBack = tabNavigator.backStack.value.canPop ||
                    tabNavigator.tabBackStack.value.canPopTab,
                canGoForward = forwardSteps.isNotEmpty(),
            )
        }
    }
}
