package io.github.juevigrace.diva.lib.shared

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import io.github.juevigrace.diva.lib.ui.theme.AppTypography
import io.github.juevigrace.diva.lib.ui.theme.darkScheme
import io.github.juevigrace.diva.lib.ui.theme.lightScheme
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
        )
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
                }
            )
        }
    }
}
