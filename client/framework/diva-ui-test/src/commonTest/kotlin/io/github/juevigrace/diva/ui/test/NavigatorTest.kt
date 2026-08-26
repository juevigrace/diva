package io.github.juevigrace.diva.ui.test

import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.ui.navigation.DefaultNavigator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

data class TestKey(val id: String) : NavKey

class NavigatorTest {

    private fun navigator(vararg keys: String): DefaultNavigator {
        val nav = DefaultNavigator(TestKey(keys.first()))
        keys.drop(1).forEach { key -> nav.navigate(TestKey(key)) }
        return nav
    }

    @Test
    fun startsAtStartDestination() {
        val nav = navigator("home")
        assertEquals(TestKey("home"), nav.backStack.value.startDestination)
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
        assertTrue(nav.backStack.value.current.isSome)
    }

    @Test
    fun navigatePushesEntries() {
        val nav = navigator("home", "search")
        assertEquals(
            listOf(TestKey("home"), TestKey("search")),
            nav.backStack.value.entries,
        )
    }

    @Test
    fun navigateSkipsSameTopByDefault() {
        val nav = navigator("home", "search")
        nav.navigate(TestKey("search"))
        assertEquals(2, nav.backStack.value.entries.size)
    }

    @Test
    fun navigateWithLaunchSingleTopFalseAllowsDuplicates() {
        val nav = DefaultNavigator(TestKey("home"))
        nav.navigate(TestKey("search"), launchSingleTop = false)
        nav.navigate(TestKey("search"), launchSingleTop = false)
        assertEquals(3, nav.backStack.value.entries.size)
    }

    @Test
    fun navigateStillPushesDifferentTop() {
        val nav = navigator("home")
        nav.navigate(TestKey("search"))
        assertEquals(2, nav.backStack.value.entries.size)
    }

    @Test
    fun popRemovesTopEntry() {
        val nav = navigator("home", "search")
        assertTrue(nav.pop())
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
    }

    @Test
    fun popNeverEmptiesTheStack() {
        val nav = navigator("home")
        assertFalse(nav.pop())
        assertEquals(listOf(TestKey("home")), nav.backStack.value.entries)
    }

    @Test
    fun popUntilTruncatesToDestination() {
        val nav = navigator("home", "a", "b", "c")
        nav.popUntil(TestKey("a"))
        assertEquals(listOf(TestKey("home"), TestKey("a")), nav.backStack.value.entries)
    }

    @Test
    fun popUntilKeepsStackWhenDestinationMissing() {
        val nav = navigator("home", "a")
        nav.popUntil(TestKey("missing"))
        assertEquals(listOf(TestKey("home"), TestKey("a")), nav.backStack.value.entries)
    }

    @Test
    fun replaceTopSwapsLastEntry() {
        val nav = navigator("home", "a")
        nav.replaceTop(TestKey("b"))
        assertEquals(listOf(TestKey("home"), TestKey("b")), nav.backStack.value.entries)
    }

    @Test
    fun replaceAllResetsToSingleEntry() {
        val nav = navigator("home", "a", "b")
        nav.replaceAll(TestKey("login"))
        assertEquals(listOf(TestKey("login")), nav.backStack.value.entries)
    }

    @Test
    fun replaceAllUpdatesStartDestination() {
        val nav = DefaultNavigator(TestKey("home"))
        assertEquals(TestKey("home"), nav.backStack.value.startDestination)
        nav.replaceAll(TestKey("other"))
        assertEquals(TestKey("other"), nav.backStack.value.entries.first())
    }

    @Test
    fun currentReflectsTopOfStack() {
        val nav = navigator("home")
        assertEquals(TestKey("home"), nav.backStack.value.current.getOrNull())
        nav.navigate(TestKey("search"))
        assertEquals(TestKey("search"), nav.backStack.value.current.getOrNull())
        nav.pop()
        assertEquals(TestKey("home"), nav.backStack.value.current.getOrNull())
    }
}
