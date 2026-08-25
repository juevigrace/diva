package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable

// iOS has no system back gesture hook in Compose Multiplatform yet
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
