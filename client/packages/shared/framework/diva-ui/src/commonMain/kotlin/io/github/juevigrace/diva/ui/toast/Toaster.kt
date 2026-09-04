package io.github.juevigrace.diva.ui.toast

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.Some
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ToastRequest(
    val message: StringResource,
    val details: Option<StringResource> = None,
    val actionLabel: Option<StringResource> = None,
    val withDismissAction: Boolean = actionLabel is Some,
    val duration: SnackbarDuration = if (actionLabel is None) {
        SnackbarDuration.Short
    } else {
        SnackbarDuration.Indefinite
    },
    val isError: Boolean = false,
)

sealed interface ToastEvent {
    data class Show(val request: ToastRequest) : ToastEvent
}

@Stable
interface Toaster {
    val events: Flow<ToastEvent>
    fun show(request: ToastRequest)

    companion object {
        fun create(): Toaster {
            return DefaultToaster()
        }
    }
}

val LocalToaster: ProvidableCompositionLocal<Toaster> =
    staticCompositionLocalOf { Toaster.create() }
