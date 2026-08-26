package io.github.juevigrace.diva.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogProperties
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.ui.navigation.BackHandler
import io.github.juevigrace.diva.ui.util.ObserveFlow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DialogHost(
    controller: DialogController = LocalDialogController.current,
    onConfirm: (DialogRequest) -> Unit = {},
    onDismiss: (DialogRequest) -> Unit = {},
    modifier: Modifier = Modifier,
    buttonArrangement: Arrangement.Horizontal = Arrangement.End,
    containerColor: Color = AlertDialogDefaults.containerColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    confirmButtonContentColor: Color = MaterialTheme.colorScheme.primary,
    dismissButtonContentColor: Color = MaterialTheme.colorScheme.primary,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    shape: Shape = AlertDialogDefaults.shape,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
) {
    var activeRequest by remember { mutableStateOf<DialogRequest?>(null) }

    ObserveFlow(flow = controller.events) { event ->
        activeRequest = when (event) {
            is DialogEvent.Show -> event.request
            is DialogEvent.Dismiss -> null
        }
    }

    BackHandler(enabled = activeRequest != null) {
        controller.dismiss()
    }

    activeRequest?.let { request ->
        DivaAlertDialog(
            request = request,
            onConfirm = {
                activeRequest = null
                controller.dismiss()
                onConfirm(request)
            },
            onDismiss = {
                activeRequest = null
                controller.dismiss()
                onDismiss(request)
            },
            modifier = modifier,
            buttonArrangement = buttonArrangement,
            containerColor = containerColor,
            titleContentColor = titleContentColor,
            textContentColor = textContentColor,
            confirmButtonContentColor = confirmButtonContentColor,
            dismissButtonContentColor = dismissButtonContentColor,
            iconContentColor = iconContentColor,
            shape = shape,
            tonalElevation = tonalElevation,
        )
    }
}

@Composable
fun DivaAlertDialog(
    request: DialogRequest,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier,
    buttonArrangement: Arrangement.Horizontal = Arrangement.End,
    containerColor: Color = AlertDialogDefaults.containerColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    confirmButtonContentColor: Color = MaterialTheme.colorScheme.primary,
    dismissButtonContentColor: Color = MaterialTheme.colorScheme.primary,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    shape: Shape = AlertDialogDefaults.shape,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
) {
    val icon = when (request.icon) {
        is Option.Some -> painterResource(request.icon.value)
        is Option.None -> null
    }
    val confirmLabel = when (request.confirmLabel) {
        is Option.Some -> stringResource(request.confirmLabel.value)
        is Option.None -> "OK"
    }
    val dismissLabel = when (request.dismissLabel) {
        is Option.Some -> stringResource(request.dismissLabel.value)
        is Option.None -> "Cancel"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        icon = request.icon.getOrNull()?.let { { Image(painter = painterResource(it), contentDescription = null) } },
        title = request.title.getOrNull()?.let { { Text(text = stringResource(it)) } },
        text = request.message.getOrNull()?.let { { Text(text = stringResource(it)) } },
        confirmButton = {
            Row(horizontalArrangement = buttonArrangement) {
                if (request.showConfirmButton) {
                    TextButton(onClick = onConfirm) {
                        Text(text = confirmLabel, color = confirmButtonContentColor)
                    }
                }
                if (request.showConfirmButton && request.showDismissButton) {
                    TextButton(onClick = onDismiss) {
                        Text(text = dismissLabel, color = dismissButtonContentColor)
                    }
                }
            }
        },
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        shape = shape,
        properties = DialogProperties(
            dismissOnBackPress = request.dismissOnBackPress,
            dismissOnClickOutside = request.dismissOnClickOutside,
        ),
    )
}
