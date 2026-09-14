package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable
import androidx.activity.compose.BackHandler as ActivityBackHandler

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    ActivityBackHandler(enabled = enabled, onBack = onBack)
}
