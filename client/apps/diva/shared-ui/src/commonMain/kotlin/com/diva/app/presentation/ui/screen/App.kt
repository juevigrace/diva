package com.diva.app.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.features.home.presentation.ui.components.navigation.homeNav
import com.diva.app.presentation.ui.theme.AppTypography
import com.diva.app.presentation.ui.theme.darkScheme
import com.diva.app.presentation.ui.theme.lightScheme
import com.diva.app.ui.navigation.HomeRoute
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation.authNav
import io.github.juevigrace.diva.lib.settings.presentation.ui.components.navigation.settingsNav
import io.github.juevigrace.diva.ui.DivaApp
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import io.github.juevigrace.diva.ui.navigation.BackStack
import io.github.juevigrace.diva.ui.navigation.NavHost
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.theme.DivaThemeConfig
import io.github.juevigrace.diva.ui.theme.ThemeScheme
import org.koin.compose.koinInject

@Composable
fun App() {
    val navigator: Navigator = koinInject()
    val backStack: BackStack by navigator.backStack.collectAsStateWithLifecycle()
    val tabNavigator: TabNavigator = koinInject()
    val tabBackStack by tabNavigator.backStack.collectAsStateWithLifecycle()

    BackHandler(
        enabled = backStack.current.getOrNull() == HomeRoute && (tabBackStack.canPop || tabBackStack.canPopTab),
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
        Screen { _ ->
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navigator = navigator,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    homeNav()
                    settingsNav()
                    authNav()
                }
            )
        }
    }
}
