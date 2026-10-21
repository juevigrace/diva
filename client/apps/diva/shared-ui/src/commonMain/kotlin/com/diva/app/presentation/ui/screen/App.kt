package com.diva.app.presentation.ui.screen

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation.authNav
import io.github.juevigrace.diva.lib.onboarding.presentation.ui.components.navigation.onboardingNav
import io.github.juevigrace.diva.lib.verification.presentation.ui.components.navigation.verificationNav
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

@Composable
fun App() {
    val rootNav: Navigator = koinInject()
    val rootStack: BackStack by rootNav.backStack.collectAsStateWithLifecycle()
    val tabNav: TabNavigator = koinInject()
    val tabStack by tabNav.tabBackStack.collectAsStateWithLifecycle()
    // The tab navigator's own linear stack, which is not the root navigator's backStack above.
    val tabInnerStack by tabNav.backStack.collectAsStateWithLifecycle()

    val appViewModel: AppViewModel = koinViewModel()
    val appState by appViewModel.state.collectAsStateWithLifecycle()

    BackHandler(
        enabled = rootStack.current == HomeRoute && (tabInnerStack.canPop || tabStack.canPopTab),
        onBack = {
            if (!tabNav.pop()) {
                tabNav.popTab()
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
            LocalNavigator provides rootNav,
            LocalTabNavigator provides tabNav,
        ) {
            Screen(
                snackBarHost = { ToasterHost() }
            ) { innerPadding ->
                NavHost(
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                        .fillMaxSize(),
                    navigator = rootNav,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider {
                        homeNav()
                        playerNav()
                        authNav()
                        onboardingNav()
                        verificationNav()
                    }
                )
            }
        }
    }
}
