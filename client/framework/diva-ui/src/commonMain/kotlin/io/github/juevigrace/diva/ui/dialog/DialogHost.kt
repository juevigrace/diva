package io.github.juevigrace.diva.ui.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.ui.navigation.BackHandler

@Stable
class DialogController {
    var current: Option<DialogRequest> by mutableStateOf(Option.None)
        private set

    fun show(request: DialogRequest) {
        current = Option.of(request)
    }

    fun dismiss() {
        current = Option.None
    }
}

@Immutable
data class DialogRequest(
    val id: String,
    val title: String = "",
    val message: String = "",
)

@Composable
fun rememberDialogController(): DialogController {
    return remember { DialogController() }
}

@Composable
fun DialogHost(
    controller: DialogController,
    confirmLabel: String = "OK",
    dismissLabel: String = "Cancel",
    onConfirm: (DialogRequest) -> Unit = {},
    onDismiss: (DialogRequest) -> Unit = {},
    dialog: @Composable (DialogRequest) -> Unit = { request ->
        DefaultDialog(request, controller, confirmLabel, dismissLabel, onConfirm, onDismiss)
    },
) {
    BackHandler(enabled = controller.current.isSome) {
        controller.dismiss()
    }

    controller.current.fold(
        onSome = { request -> dialog(request) },
        onNone = {},
    )
}

@Composable
private fun DefaultDialog(
    request: DialogRequest,
    controller: DialogController,
    confirmLabel: String,
    dismissLabel: String,
    onConfirm: (DialogRequest) -> Unit,
    onDismiss: (DialogRequest) -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            controller.dismiss()
            onDismiss(request)
        },
        title = if (request.title.isEmpty()) null else {
            { Text(text = request.title) }
        },
        text = if (request.message.isEmpty()) null else {
            { Text(text = request.message) }
        },
        confirmButton = {
            TextButton(onClick = {
                controller.dismiss()
                onConfirm(request)
            }) {
                Text(text = confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                controller.dismiss()
                onDismiss(request)
            }) {
                Text(text = dismissLabel)
            }
        },
    )
}
