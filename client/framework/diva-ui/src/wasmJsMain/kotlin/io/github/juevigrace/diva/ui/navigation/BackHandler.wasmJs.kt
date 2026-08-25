package io.github.juevigrace.diva.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.browser.window
import org.w3c.dom.events.Event
import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalWasmJsInterop::class)
@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    val currentOnBack = rememberUpdatedState(onBack)
    if (enabled) {
        DisposableEffect(Unit) {
            val handler: (Event) -> Unit = {
                currentOnBack.value.invoke()
                window.history.forward()
            }
            window.addEventListener("popstate", handler)
            window.history.pushState(null, "", null)
            onDispose {
                window.removeEventListener("popstate", handler)
            }
        }
    }
}
