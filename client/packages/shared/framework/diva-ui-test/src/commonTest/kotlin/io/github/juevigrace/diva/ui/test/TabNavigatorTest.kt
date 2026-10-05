package io.github.juevigrace.diva.ui.test

import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.ui.navigation.Tab
import io.github.juevigrace.diva.ui.navigation.TabBackStack
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(InternalResourceApi::class)
private fun testDrawable(id: String): DrawableResource =
    DrawableResource(id, setOf(ResourceItem(setOf(), id, 0, 0)))

@OptIn(InternalResourceApi::class)
private fun testTitle(id: String): StringResource =
    StringResource(id, id, setOf(ResourceItem(setOf(), id, 0, 0)))

data object TestHomeTab : Tab {
    override val route: NavKey = TestKey("home")
    override val icon: DrawableResource = testDrawable("home")
    override val title: StringResource = testTitle("home")
}

data object TestLibraryTab : Tab {
    override val route: NavKey = TestKey("library")
    override val icon: DrawableResource = testDrawable("library")
    override val title: StringResource = testTitle("library")
}

data object TestProfileTab : Tab {
    override val route: NavKey = TestKey("profile")
    override val icon: DrawableResource = testDrawable("profile")
    override val title: StringResource = testTitle("profile")
}

data object TestUnknownTab : Tab {
    override val route: NavKey = TestKey("unknown")
    override val icon: DrawableResource = testDrawable("unknown")
    override val title: StringResource = testTitle("unknown")
}

private val allTestTabs = listOf(TestHomeTab, TestLibraryTab, TestProfileTab)

class TabNavigatorTest {

    private fun tabNavigator(start: Tab = TestHomeTab): TabNavigator =
        TabNavigator.create(allTestTabs, start)

    @Test
    fun startsAtStartTab() {
        val nav = tabNavigator()
        assertEquals(TestHomeTab, nav.tabBackStack.value.selectedTab)
        assertEquals(listOf(TestHomeTab), nav.tabBackStack.value.tabHistory)
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
        assertFalse(nav.backStack.value.canPop)
        assertFalse(nav.tabBackStack.value.canPopTab)
    }

    /**
     * The whole point of extending BaseNavigator: linear ops on the TabNavigator itself must
     * reach the stack the host renders.
     */
    @Test
    fun inheritedLinearOpsDriveTheRenderedStack() {
        val nav = tabNavigator()

        nav.navigate(TestKey("search"))
        assertEquals(listOf(TestKey("home"), TestKey("search")), nav.backStack.value.entries)
        assertTrue(nav.backStack.value.canPop)

        assertTrue(nav.pop())
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)

        nav.navigate(TestKey("a"))
        nav.navigate(TestKey("b"))
        nav.popUntil(TestKey("a"))
        assertEquals(listOf(TestKey("home"), TestKey("a")), nav.backStack.value.entries)

        nav.replaceTop(TestKey("c"))
        assertEquals(listOf(TestKey("home"), TestKey("c")), nav.backStack.value.entries)

        nav.replaceAll(TestKey("home"))
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
    }

    @Test
    fun selectTabSwapsTheLinearStack() {
        val nav = tabNavigator()
        nav.navigate(TestKey("search"))

        nav.selectTab(TestLibraryTab)

        assertEquals(TestLibraryTab, nav.tabBackStack.value.selectedTab)
        assertEquals(listOf(TestKey("library")), nav.backStack.value.entries)
        assertFalse(nav.backStack.value.canPop)
        assertEquals(
            listOf(TestHomeTab, TestLibraryTab),
            nav.tabBackStack.value.tabHistory,
        )
        assertTrue(nav.tabBackStack.value.canPopTab)
    }

    /** The regression this whole refactor exists to prevent. */
    @Test
    fun eachTabRestoresItsOwnStack() {
        val nav = tabNavigator()
        nav.navigate(TestKey("search"))

        nav.selectTab(TestLibraryTab)
        nav.navigate(TestKey("album"))

        nav.selectTab(TestProfileTab)
        assertEquals(listOf(TestKey("profile")), nav.backStack.value.entries)

        nav.selectTab(TestHomeTab)
        assertEquals(listOf(TestKey("home"), TestKey("search")), nav.backStack.value.entries)

        nav.selectTab(TestLibraryTab)
        assertEquals(listOf(TestKey("library"), TestKey("album")), nav.backStack.value.entries)
    }

    @Test
    fun selectTabIgnoresUnknownAndAlreadySelectedTabs() {
        val nav = tabNavigator()
        nav.navigate(TestKey("search"))

        nav.selectTab(TestHomeTab)
        assertEquals(listOf(TestKey("home"), TestKey("search")), nav.backStack.value.entries)
        assertEquals(listOf(TestHomeTab), nav.tabBackStack.value.tabHistory)

        nav.selectTab(TestUnknownTab)
        assertEquals(TestHomeTab, nav.tabBackStack.value.selectedTab)
    }

    @Test
    fun popTabRestoresThePreviousTabsStack() {
        val nav = tabNavigator()
        nav.navigate(TestKey("search"))
        nav.selectTab(TestLibraryTab)
        nav.navigate(TestKey("album"))

        assertTrue(nav.popTab())

        assertEquals(TestHomeTab, nav.tabBackStack.value.selectedTab)
        assertEquals(listOf(TestHomeTab), nav.tabBackStack.value.tabHistory)
        assertEquals(listOf(TestKey("home"), TestKey("search")), nav.backStack.value.entries)
        assertFalse(nav.tabBackStack.value.canPopTab)
        assertFalse(nav.popTab())
    }

    @Test
    fun popTabUntilTruncatesHistoryAndSwapsStack() {
        val nav = tabNavigator()
        nav.selectTab(TestLibraryTab)
        nav.selectTab(TestProfileTab)
        nav.navigate(TestKey("settings"))

        assertTrue(nav.popTabUntil(TestLibraryTab))

        assertEquals(TestLibraryTab, nav.tabBackStack.value.selectedTab)
        assertEquals(listOf(TestHomeTab, TestLibraryTab), nav.tabBackStack.value.tabHistory)
        assertEquals(listOf(TestKey("library")), nav.backStack.value.entries)

        // a tab that was never visited cannot be popped to
        assertFalse(nav.popTabUntil(TestUnknownTab))

        // inclusive, like popUntil: landing on Home truncates history down to Home itself
        assertTrue(nav.popTabUntil(TestHomeTab))
        assertEquals(TestHomeTab, nav.tabBackStack.value.selectedTab)
        assertEquals(listOf(TestHomeTab), nav.tabBackStack.value.tabHistory)
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
    }

    @Test
    fun clearTabHistoryKeepsTheSelectedTab() {
        val nav = tabNavigator()
        nav.selectTab(TestLibraryTab)
        nav.selectTab(TestProfileTab)

        nav.clearTabHistory()

        assertEquals(TestProfileTab, nav.tabBackStack.value.selectedTab)
        assertEquals(listOf(TestProfileTab), nav.tabBackStack.value.tabHistory)
        assertFalse(nav.tabBackStack.value.canPopTab)
    }

    @Test
    fun navigateRejectsTabRoots() {
        val nav = tabNavigator()
        val error = assertFailsWith<IllegalArgumentException> {
            nav.navigate(TestKey("library"))
        }
        assertTrue(error.message.orEmpty().contains("selectTab"))
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
    }

    /** selectedTab is derived from tabHistory.last(), so the two cannot drift apart. */
    @Test
    fun selectedTabIsAlwaysTheLastOfTabHistory() {
        val nav = tabNavigator()
        assertEquals(nav.tabBackStack.value.tabHistory.last(), nav.tabBackStack.value.selectedTab)

        nav.selectTab(TestLibraryTab)
        nav.navigate(TestKey("album"))
        nav.selectTab(TestProfileTab)
        assertEquals(nav.tabBackStack.value.tabHistory.last(), nav.tabBackStack.value.selectedTab)

        nav.popTab()
        assertEquals(nav.tabBackStack.value.tabHistory.last(), nav.tabBackStack.value.selectedTab)
        assertEquals(TestLibraryTab, nav.tabBackStack.value.selectedTab)
    }

    @Test
    fun requiresANonEmptyTabHistory() {
        assertFailsWith<IllegalArgumentException> {
            TabBackStack(tabHistory = emptyList(), savedStacks = emptyMap())
        }
        assertFailsWith<IllegalArgumentException> {
            TabBackStack(
                tabHistory = listOf(TestHomeTab),
                savedStacks = emptyMap(),
            ).copy(tabHistory = emptyList())
        }
    }
}
