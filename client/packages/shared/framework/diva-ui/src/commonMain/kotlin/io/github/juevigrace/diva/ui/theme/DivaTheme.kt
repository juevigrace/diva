package io.github.juevigrace.diva.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
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

@Immutable
@Stable
data class ThemeScheme(
    val light: ColorScheme = lightColorScheme(),
    val dark: ColorScheme = darkColorScheme(),
)

@Immutable
@Stable
data class DivaThemeConfig(
    val themeScheme: ThemeScheme = ThemeScheme(),
    val typography: Typography = Typography(),
    val shapes: Shapes = Shapes(),
    val useDynamicColors: Boolean = false,
)
