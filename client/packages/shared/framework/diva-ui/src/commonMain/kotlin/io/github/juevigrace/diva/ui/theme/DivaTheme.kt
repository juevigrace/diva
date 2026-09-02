package io.github.juevigrace.diva.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import io.github.juevigrace.diva.core.getOrElse

@Composable
fun DivaTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    config: DivaThemeConfig = DivaThemeConfig(),
    systemUiConfig: @Composable () -> Unit = {
        ConfigureSystemUI(isDark, config.themeScheme)
    },
    content: @Composable () -> Unit,
) {
    systemUiConfig()
    val configuredScheme = config.themeScheme
    val colorScheme = if (config.useDynamicColors) {
        dynamicColorScheme(isDark).getOrElse {
            if (isDark) configuredScheme.dark else configuredScheme.light
        }
    } else {
        if (isDark) configuredScheme.dark else configuredScheme.light
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = config.typography,
        shapes = config.shapes,
        content = content,
    )
}
