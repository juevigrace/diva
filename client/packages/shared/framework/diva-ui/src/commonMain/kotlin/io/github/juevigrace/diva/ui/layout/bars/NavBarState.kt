package io.github.juevigrace.diva.ui.layout.bars

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.ui.navigation.Tab

// TODO: rework this with the tab navigator
interface NavBarState {
    val tabs: List<Tab>
        get() = emptyList()
    val selectedTabIndex: Int
        get() = 0
    val showBar: Boolean
        get() = true

    val selectedTab: Option<Tab>
        get() = Option.of(tabs.getOrNull(selectedTabIndex))

    fun updateIndex(index: Int): NavBarState

    fun selectTab(index: Int): NavBarState {
        return when {
            tabs.isEmpty() -> this
            index < 0 || index >= tabs.size -> this
            isSelected(index) -> this
            else -> updateIndex(index)
        }
    }

    fun selectTab(tab: Tab): NavBarState {
        return when {
            tabs.isEmpty() -> this
            !tabs.contains(tab) -> this
            isSelected(tab) -> this
            else -> updateIndex(tabs.indexOf(tab))
        }
    }

    fun toggleBar(): NavBarState

    fun hideBar(): NavBarState {
        if (!isVisible()) {
            return this
        }
        return toggleBar()
    }

    fun showBar(): NavBarState {
        if (isVisible()) {
            return this
        }
        return toggleBar()
    }

    fun isVisible(): Boolean {
        return showBar
    }

    fun isSelected(tab: Tab): Boolean {
        return selectedTab.map { it == tab }.getOrElse { false }
    }

    fun isSelected(index: Int): Boolean {
        return selectedTabIndex == index
    }
}

@Stable
class DefaultNavBarState(
    initialTabs: List<Tab> = emptyList(),
    initialSelectedTabIndex: Int = 0,
    initialShowBar: Boolean = true,
) : NavBarState {
    override var tabs: List<Tab> by mutableStateOf(initialTabs)
        private set

    override var selectedTabIndex: Int by mutableStateOf(initialSelectedTabIndex)
        private set

    override var showBar: Boolean by mutableStateOf(initialShowBar)
        private set

    override fun updateIndex(index: Int): NavBarState {
        selectedTabIndex = index
        return this
    }

    override fun toggleBar(): NavBarState {
        showBar = !showBar
        return this
    }
}
