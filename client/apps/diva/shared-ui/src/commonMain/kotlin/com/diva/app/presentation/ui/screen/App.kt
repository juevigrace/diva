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
import com.diva.app.collection.presentation.ui.components.navigation.albumNav
import com.diva.app.feed.presentation.ui.components.navigation.feedNav
import com.diva.app.folder.presentation.ui.components.navigation.folderNav
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.ui.components.navigation.homeNav
import com.diva.app.library.presentation.ui.components.navigation.libraryNav
import com.diva.app.media.presentation.ui.components.navigation.mediaNav
import com.diva.app.mix.presentation.ui.components.navigation.mixNav
import com.diva.app.player.presentation.ui.components.navigation.playerNav
import com.diva.app.playlist.presentation.ui.components.navigation.playlistNav
import com.diva.app.profile.presentation.ui.components.navigation.profileNav
import com.diva.app.presentation.ui.theme.AppTypography
import com.diva.app.presentation.ui.theme.darkScheme
import com.diva.app.presentation.ui.theme.lightScheme
import com.diva.app.presentation.viewmodel.AppViewModel
import com.diva.app.search.presentation.ui.components.navigation.searchNav
import com.diva.app.server.presentation.ui.components.navigation.serverNav
import com.diva.app.settings.presentation.ui.components.navigation.settingsNav
import io.github.juevigrace.diva.lib.auth.presentation.ui.components.navigation.authNav
import io.github.juevigrace.diva.lib.devices.presentation.ui.components.navigation.devicesNav
import io.github.juevigrace.diva.lib.onboarding.presentation.ui.components.navigation.onboardingNav
import io.github.juevigrace.diva.lib.permissions.presentation.ui.components.navigation.permissionsNav
import io.github.juevigrace.diva.lib.session.presentation.ui.components.navigation.sessionNav
import io.github.juevigrace.diva.lib.user.presentation.ui.components.navigation.userNav
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
            ) { _ ->
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navigator = rootNav,
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    entryProvider = entryProvider {
                        homeNav()
                        playerNav()
                        settingsNav()
                        feedNav()
                        albumNav()
                        mediaNav()
                        mixNav()
                        playlistNav()
                        serverNav()
                        folderNav()
                        libraryNav()
                        profileNav()
                        searchNav()
                        authNav()
                        sessionNav()
                        userNav()
                        devicesNav()
                        permissionsNav()
                        onboardingNav()
                        verificationNav()
                    }
                )
            }
        }
    }
}
