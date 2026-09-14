package io.github.juevigrace.diva.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
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
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlin.collections.plus


interface Navigator {
    val backStack: StateFlow<BackStack>

    fun navigate(destination: NavKey, launchSingleTop: Boolean = true)
    fun pop(): Boolean
    fun popUntil(destination: NavKey)
    fun replaceTop(destination: NavKey)
    fun replaceAll(destination: NavKey)

    companion object {
        fun create(startDestination: NavKey): Navigator = NavigatorImpl(startDestination)
    }
}

@Immutable
data class BackStack(
    val startDestination: NavKey,
    val entries: List<NavKey>,
) {
    val current: Option<NavKey>
        get() = Option.of(entries.lastOrNull())
}

@Stable
internal class NavigatorImpl(startDestination: NavKey) : Navigator {
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

val LocalNavigator = staticCompositionLocalOf<Navigator> { error("No Navigator provided") }

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
    val navigator = remember { NavigatorImpl(startDestination) }
    if (navBackStack != null) {
        LaunchedEffect(navBackStack) {
            snapshotFlow { navBackStack.toList() }
                .distinctUntilChanged()
                .collect { entries ->
                    navigator.syncFromBackStack(entries)
                }
        }
    }
    return navigator
}

@Composable
fun NavHost(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onBack: () -> Unit = { navigator.pop() },
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
    val backStack: BackStack by navigator.backStack.collectAsStateWithLifecycle()
    CompositionLocalProvider(LocalNavigator provides navigator) {
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
