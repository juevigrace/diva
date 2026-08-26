package io.github.juevigrace.diva.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SinglePaneSceneStrategy
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.defaultPopTransitionSpec
import androidx.navigation3.ui.defaultPredictivePopTransitionSpec
import androidx.navigation3.ui.defaultTransitionSpec
import androidx.navigationevent.NavigationEvent

@Composable
fun NavHost(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = { navigator.pop() },
    entryProvider: (key: NavKey) -> NavEntry<NavKey>,
) {
    NavHost(
        navigator = navigator,
        modifier = modifier,
        onBack = onBack,
        entryProvider = entryProvider,
        sizeTransform = null,
        transitionSpec = defaultTransitionSpec(),
        popTransitionSpec = defaultPopTransitionSpec(),
        predictivePopTransitionSpec = defaultPredictivePopTransitionSpec(),
    )
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
