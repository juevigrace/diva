package io.github.juevigrace.diva.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.remember
import io.github.juevigrace.diva.ui.navigation.BackHandler
import io.github.juevigrace.diva.ui.util.ObserveFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@Immutable
data class DialogRequest(
    val id: String,
    val title: String = "",
    val message: String = "",
)

@Stable
interface DialogController {
    val requests: Flow<DialogRequest>
    val current: StateFlow<DialogRequest?>
    fun show(request: DialogRequest)
    fun dismiss()
}

class DefaultDialogController : DialogController {
    private val _requests: Channel<DialogRequest> = Channel(capacity = Channel.UNLIMITED)

    override val requests: Flow<DialogRequest>
        get() = _requests.receiveAsFlow()

    override val current: StateFlow<DialogRequest?>
        field = MutableStateFlow<DialogRequest?>(null)

    override fun show(request: DialogRequest) {
        _requests.trySend(request)
        current.update { request }
    }

    override fun dismiss() {
        current.update { null }
    }
}

val LocalDialogController: ProvidableCompositionLocal<DialogController> =
    staticCompositionLocalOf { DefaultDialogController() }

@Composable
fun rememberDialogController(): DialogController {
    return remember { DefaultDialogController() }
}

@Composable
fun DialogHost(
    controller: DialogController = LocalDialogController.current,
    confirmLabel: String = "OK",
    dismissLabel: String = "Cancel",
    onConfirm: (DialogRequest) -> Unit = {},
    onDismiss: (DialogRequest) -> Unit = {},
    dialog: @Composable (DialogRequest) -> Unit = { request ->
        DivaAlertDialog(
            request = request,
            onConfirm = {
                controller.dismiss()
                onConfirm(request)
            },
            onDismiss = {
                controller.dismiss()
                onDismiss(request)
            },
            confirmLabel = confirmLabel,
            dismissLabel = dismissLabel,
        )
    },
) {
    BackHandler(enabled = controller.current.value != null) {
        controller.dismiss()
    }

    ObserveFlow(flow = controller.requests) { request ->
        // Request handled by controller.current update
    }

    controller.current.value?.let { request ->
        dialog(request)
    }
}

@Composable
fun DivaAlertDialog(
    request: DialogRequest,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    confirmLabel: String = "OK",
    dismissLabel: String = "Cancel",
    titleContent: @Composable (() -> Unit)? = if (request.title.isEmpty()) null else {
        { Text(text = request.title) }
    },
    textContent: @Composable (() -> Unit)? = if (request.message.isEmpty()) null else {
        { Text(text = request.message) }
    },
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = titleContent,
        text = textContent,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissLabel)
            }
        },
    )
}
