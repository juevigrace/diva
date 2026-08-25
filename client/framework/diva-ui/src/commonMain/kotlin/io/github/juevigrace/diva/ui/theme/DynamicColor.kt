package io.github.juevigrace.diva.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import io.github.juevigrace.diva.core.Option

// Some when the platform provides a dynamic scheme for the current darkness, None otherwise.
// Callers fall back to their configured ThemeScheme via getOrElse.
@Composable
expect fun dynamicColorScheme(isDark: Boolean): Option<ColorScheme>
