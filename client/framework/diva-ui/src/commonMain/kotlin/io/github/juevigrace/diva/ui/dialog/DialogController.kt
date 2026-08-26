package io.github.juevigrace.diva.ui.dialog

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Immutable
data class DialogRequest(
    val title: Option<StringResource> = Option.None,
    val message: Option<StringResource> = Option.None,
    val icon: Option<DrawableResource> = Option.None,
    val confirmLabel: Option<StringResource> = Option.None,
    val dismissLabel: Option<StringResource> = Option.None,
    val showConfirmButton: Boolean = true,
    val showDismissButton: Boolean = true,
    val dismissOnBackPress: Boolean = true,
    val dismissOnClickOutside: Boolean = true,
)

sealed interface DialogEvent {
    data class Show(val request: DialogRequest) : DialogEvent
    data object Dismiss : DialogEvent
}

@Stable
interface DialogController {
    val events: Flow<DialogEvent>
    fun show(request: DialogRequest)
    fun dismiss()

    companion object {
        fun create(): DialogController {
            return DefaultDialogController()
        }
    }
}

val LocalDialogController: ProvidableCompositionLocal<DialogController> =
    staticCompositionLocalOf { DialogController.create() }
