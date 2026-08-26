package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Stable
internal class DefaultNavigator(
    startDestination: NavKey,
) : Navigator {

    override val backStack: StateFlow<BackStack>
        field = MutableStateFlow(
            BackStack(startDestination = startDestination, entries = listOf(startDestination))
        )

    internal fun syncFromBackStack(entries: List<NavKey>) {
        backStack.update { state ->
            state.copy(entries = entries)
        }
    }

    override fun navigate(destination: NavKey, launchSingleTop: Boolean) {
        backStack.update { state ->
            if (launchSingleTop && state.entries.lastOrNull() == destination) {
                return@update state
            }
            state.copy(entries = state.entries + destination)
        }
    }

    override fun pop(): Boolean {
        var popped = false
        backStack.update { state ->
            if (state.entries.size <= 1) {
                return@update state
            }
            popped = true
            state.copy(entries = state.entries.dropLast(1))
        }
        return popped
    }

    override fun popUntil(destination: NavKey) {
        backStack.update { state ->
            val index = state.entries.lastIndexOf(destination)
            if (index == -1) {
                return@update state
            }
            state.copy(entries = state.entries.take(index + 1))
        }
    }

    override fun replaceTop(destination: NavKey) {
        backStack.update { state ->
            if (state.entries.isEmpty() || state.entries.last() == destination) {
                return@update state
            }
            state.copy(entries = state.entries.dropLast(1) + destination)
        }
    }

    override fun replaceAll(destination: NavKey) {
        backStack.update { state ->
            state.copy(entries = listOf(destination))
        }
    }
}
