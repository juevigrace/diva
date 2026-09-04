package io.github.juevigrace.diva.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option

@Composable
actual fun dynamicColorScheme(isDark: Boolean): Option<ColorScheme> = None
