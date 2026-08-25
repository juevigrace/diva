package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.fold
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update

@Immutable
data class BackStack(val entries: List<NavKey>) {
    val current: Option<NavKey>
        get() = Option.of(entries.lastOrNull())
}

@Immutable
data class NavigationResult(val key: NavKey, val value: Any?)

class Navigator(
    startDestination: NavKey,
    internal val navBackStack: NavBackStack<NavKey>? = null,
) {

    val startDestination: NavKey = startDestination

    private val mutBackStack: MutableStateFlow<BackStack> = MutableStateFlow(
        BackStack(entries = listOf(startDestination))
    )

    val backStack: StateFlow<BackStack> = mutBackStack.asStateFlow()

    val current: Option<NavKey>
        get() = backStack.value.current

    private val mutResults = MutableSharedFlow<NavigationResult>(extraBufferCapacity = 1)

    val results: SharedFlow<NavigationResult>
        get() = mutResults

    internal fun syncFromNavBackStack(entries: List<NavKey>) {
        mutBackStack.update { BackStack(entries = entries) }
    }

    fun navigate(destination: NavKey, launchSingleTop: Boolean = false) {
        mutBackStack.update { state ->
            if (launchSingleTop && state.entries.lastOrNull() == destination) {
                return@update state
            }
            state.copy(entries = state.entries + destination)
        }
    }

    fun pop(): Boolean {
        var popped = false
        mutBackStack.update { state ->
            if (state.entries.size <= 1) {
                return@update state
            }
            popped = true
            state.copy(entries = state.entries.dropLast(1))
        }
        return popped
    }

    fun popUntil(destination: NavKey) {
        mutBackStack.update { state ->
            val index = state.entries.lastIndexOf(destination)
            if (index == -1) {
                return@update state
            }
            state.copy(entries = state.entries.take(index + 1))
        }
    }

    fun replaceTop(destination: NavKey) {
        mutBackStack.update { state ->
            if (state.entries.isEmpty() || state.entries.last() == destination) {
                return@update state
            }
            state.copy(entries = state.entries.dropLast(1) + destination)
        }
    }

    fun replaceAll(destination: NavKey) {
        mutBackStack.update { BackStack(entries = listOf(destination)) }
    }

    fun popWithResult(result: Any?): Boolean {
        return backStack.value.current.fold(
            onSome = { top ->
                if (pop()) {
                    mutResults.tryEmit(NavigationResult(key = top, value = result))
                    true
                } else {
                    false
                }
            },
            onNone = { false },
        )
    }

    suspend fun <R> navigateForResult(destination: NavKey): R {
        navigate(destination)
        val navigationResult = results.first { it.key == destination }
        @Suppress("UNCHECKED_CAST")
        return navigationResult.value as R
    }
}

@Composable
fun rememberNavigator(
    startDestination: NavKey,
    configuration: SavedStateConfiguration? = LocalSavedStateConfiguration.current,
): Navigator {
    val navBackStack: NavBackStack<NavKey>? = if (configuration != null) {
        rememberNavBackStack(configuration, startDestination)
    } else {
        null
    }
    val navigator = remember { Navigator(startDestination, navBackStack) }
    if (navBackStack != null) {
        LaunchedEffect(navBackStack) {
            snapshotFlow { navBackStack.toList() }
                .distinctUntilChanged()
                .collect { entries ->
                    navigator.syncFromNavBackStack(entries)
                }
        }
    }
    return navigator
}
