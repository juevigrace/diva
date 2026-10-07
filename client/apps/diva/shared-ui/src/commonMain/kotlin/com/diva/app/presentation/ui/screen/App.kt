package com.diva.app.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.ui.components.navigation.homeNav
import com.diva.app.player.presentation.ui.components.navigation.playerNav
import com.diva.app.presentation.ui.theme.AppTypography
import com.diva.app.presentation.ui.theme.darkScheme
import com.diva.app.presentation.ui.theme.lightScheme
import com.diva.app.presentation.viewmodel.AppViewModel
import com.diva.app.settings.presentation.ui.components.navigation.settingsNav
import io.github.juevigrace.diva.ui.DivaApp
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import io.github.juevigrace.diva.ui.navigation.BackStack
import io.github.juevigrace.diva.ui.navigation.LocalNavigator
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator
import io.github.juevigrace.diva.ui.navigation.NavHost
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.theme.DivaThemeConfig
import io.github.juevigrace.diva.ui.theme.ThemeScheme
import io.github.juevigrace.diva.ui.toast.ToasterHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun App() {
    val navigator: Navigator = koinInject()
    val backStack: BackStack by navigator.backStack.collectAsStateWithLifecycle()
    val tabNavigator: TabNavigator = koinInject()
    val tabBackStack by tabNavigator.tabBackStack.collectAsStateWithLifecycle()
    // The tab navigator's own linear stack, which is not the root navigator's backStack above.
    val tabEntries by tabNavigator.backStack.collectAsStateWithLifecycle()

    val appViewModel: AppViewModel = koinViewModel(parameters = { parametersOf(0) })
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    BackHandler(
        enabled = backStack.current == HomeRoute && (tabEntries.canPop || tabBackStack.canPopTab),
        onBack = {
            if (!tabNavigator.pop()) {
                tabNavigator.popTab()
            }
        }
    )

    DivaApp(
        themeConfig = DivaThemeConfig(
            themeScheme = ThemeScheme(
                light = lightScheme,
                dark = darkScheme,
            ),
            typography = AppTypography,
        ),
        toaster = koinInject(),
        dialogController = koinInject(),
    ) {
        CompositionLocalProvider(
            LocalNavigator provides navigator,
            LocalTabNavigator provides tabNavigator,
        ) {
            Screen(
                snackBarHost = { ToasterHost() }
            ) { _ ->
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navigator = navigator,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider {
                        homeNav()
                        playerNav()
                        settingsNav()
                    }
                )
            }
        }
    }
}
