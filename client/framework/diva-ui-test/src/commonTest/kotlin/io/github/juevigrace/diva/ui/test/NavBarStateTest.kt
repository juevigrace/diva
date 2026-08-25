package io.github.juevigrace.diva.ui.test

import io.github.juevigrace.diva.ui.navigation.NavBarState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private data class TestNavBarState(
    override val tabs: List<io.github.juevigrace.diva.ui.navigation.Tab> = emptyList(),
    override val selectedTabIndex: Int = 0,
    override val showBar: Boolean = true,
) : NavBarState {
    override fun updateIndex(index: Int): NavBarState = copy(selectedTabIndex = index)

    override fun toggleBar(): NavBarState = copy(showBar = !showBar)
}

// Tab instances can't be built in plain unit tests: DrawableResource/StringResource
// constructors are internal to Compose Resources codegen. Selection-by-index and
// bar visibility are covered here; instance-based selection is exercised in UI tests.
class NavBarStateTest {

    private val state = TestNavBarState()

    @Test
    fun emptyStateHasNoSelectedTab() {
        assertTrue(state.selectedTab.isNone)
    }

    @Test
    fun emptyStateIgnoresSelection() {
        assertEquals(state, state.selectTab(0))
        assertEquals(state, state.selectTab(-1))
    }

    @Test
    fun defaults() {
        assertEquals(0, state.selectedTabIndex)
        assertTrue(state.showBar)
        assertTrue(state.isVisible())
        assertTrue(state.tabs.isEmpty())
    }

    @Test
    fun hideShowBarCycle() {
        val hidden = state.hideBar()
        assertFalse(hidden.isVisible())
        val shown = hidden.showBar()
        assertTrue(shown.isVisible())
    }

    @Test
    fun hideAndShowAreIdempotent() {
        assertEquals(state, state.showBar())
        val hidden = state.hideBar()
        assertEquals(hidden, hidden.hideBar())
    }
}
