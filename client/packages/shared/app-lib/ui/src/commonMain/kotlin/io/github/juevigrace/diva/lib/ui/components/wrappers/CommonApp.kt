package io.github.juevigrace.diva.lib.ui.components.wrappers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import io.github.juevigrace.diva.lib.ui.navigation.Screens
import io.github.juevigrace.diva.ui.DivaApp
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.NavHost
import io.github.juevigrace.diva.ui.theme.DivaThemeConfig
import org.koin.compose.koinInject

// TODO: this is useless for now
@Composable
fun CommonApp(
    startDestination: Screens,
    themeConfig: DivaThemeConfig = DivaThemeConfig(),
    screen: @Composable (content: @Composable (innerPadding: PaddingValues) -> Unit) -> Unit = { content ->
        Screen(content = content)
    },
) {
    DivaApp(
        themeConfig = themeConfig
    ) {
        screen { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
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
