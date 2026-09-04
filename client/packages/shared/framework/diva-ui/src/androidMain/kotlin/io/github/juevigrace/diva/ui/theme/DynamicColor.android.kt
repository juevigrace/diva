package io.github.juevigrace.diva.ui.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option

@Composable
actual fun dynamicColorScheme(isDark: Boolean): Option<ColorScheme> {
    if (Build.VERSION.SDK_INT < 31) {
        return None
    }
    val context = LocalContext.current
    return Option.of(if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context))
}
