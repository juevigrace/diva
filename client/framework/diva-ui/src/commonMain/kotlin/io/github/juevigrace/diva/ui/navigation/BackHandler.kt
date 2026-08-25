package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable

/**
 * Intercepts back navigation within the current composition.
 *
 * @param enabled When true, the back handler is active and will invoke [onBack] on back gestures.
 * @param onBack Callback invoked when a back gesture or back button press occurs.
 *
 * On web (js/wasmJs), this pushes a browser history entry so the user has something to go "back"
 * to, listens for `popstate` events, and automatically calls `window.history.forward()` to cancel
 * the browser's own navigation before invoking [onBack].
 */
@Composable
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)
