package io.github.juevigrace.diva.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import io.github.juevigrace.diva.core.Option

@Composable
expect fun dynamicColorScheme(isDark: Boolean): Option<ColorScheme>
