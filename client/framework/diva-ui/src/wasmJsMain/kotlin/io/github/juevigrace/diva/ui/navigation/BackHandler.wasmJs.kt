package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable

// browser history back integration lands with Phase 2B saved-state work
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) = Unit
