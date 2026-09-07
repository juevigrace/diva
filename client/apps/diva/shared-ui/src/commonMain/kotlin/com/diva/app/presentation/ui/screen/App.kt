package com.diva.app.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.features.home.presentation.ui.screen.HomeScreen
import com.diva.app.presentation.ui.theme.AppTypography
import com.diva.app.presentation.ui.theme.darkScheme
import com.diva.app.presentation.ui.theme.lightScheme
import com.diva.app.ui.navigation.HomeRoute
import io.github.juevigrace.diva.ui.DivaApp
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.NavHost
import io.github.juevigrace.diva.ui.theme.DivaThemeConfig
import io.github.juevigrace.diva.ui.theme.ThemeScheme
import org.koin.compose.koinInject

@Composable
fun App() {
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
        Screen {
            NavHost(
                modifier = Modifier.fillMaxSize(),
                navigator = koinInject(),
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
                entryProvider = entryProvider {
                    entry<HomeRoute> {
                        HomeScreen()
                    }
                }
            )
        }
    }
}
