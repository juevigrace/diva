package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Stable

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
