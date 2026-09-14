package io.github.juevigrace.diva.ui.dialog

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Stable
interface DialogController {
    val events: Flow<DialogEvent>
    fun show(request: DialogRequest)
    fun dismiss()

    companion object {
        fun create(): DialogController {
            return DialogControllerImpl
        }
    }
}

sealed interface DialogEvent {
    data class Show(val request: DialogRequest) : DialogEvent
    data object Dismiss : DialogEvent
}

@Immutable
data class DialogRequest(
    val title: Option<StringResource> = None,
    val message: Option<StringResource> = None,
    val icon: Option<DrawableResource> = None,
    val confirmLabel: Option<StringResource> = None,
    val dismissLabel: Option<StringResource> = None,
    val showConfirmButton: Boolean = true,
    val showDismissButton: Boolean = true,
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
)

val LocalDialogController: ProvidableCompositionLocal<DialogController> =
    staticCompositionLocalOf { DialogController.create() }

internal object DialogControllerImpl : DialogController {
    private val _events: Channel<DialogEvent> = Channel(capacity = Channel.UNLIMITED)

    override val events: Flow<DialogEvent>
        get() = _events.receiveAsFlow()

    override fun show(request: DialogRequest) {
        _events.trySend(DialogEvent.Show(request))
    }

    override fun dismiss() {
        _events.trySend(DialogEvent.Dismiss)
    }
}
