package io.github.juevigrace.diva.ui.toast

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.onSome
import io.github.juevigrace.diva.ui.util.ObserveFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

@Composable
fun ToasterHost(
    toaster: Toaster = LocalToaster.current,
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState = remember { SnackbarHostState() },
    containerColor: Color = MaterialTheme.colorScheme.inverseSurface,
    contentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
    actionColor: Color = MaterialTheme.colorScheme.inversePrimary,
    actionContentColor: Color = MaterialTheme.colorScheme.inverseOnSurface,
) {
    val scope = rememberCoroutineScope()
    var currentIsError by remember { mutableStateOf(false) }

    ObserveFlow(flow = toaster.events) { event ->
        when (event) {
            is ToastEvent.Show -> {
                val request = event.request
                currentIsError = request.isError
                scope.launch {
                    val messageText = buildString {
                        append(getString(request.message))
                        request.details.onSome { details -> append(" - ${getString(details)}") }
                    }
                    hostState.showSnackbar(
                        message = messageText,
                        actionLabel = request.actionLabel.map { getString(it) }.getOrNull(),
                        withDismissAction = request.withDismissAction,
                        duration = request.duration,
                    )
                }
            }
        }
    }

    val palette = if (currentIsError) {
        SnackbarPalette(
            container = MaterialTheme.colorScheme.errorContainer,
            content = MaterialTheme.colorScheme.onErrorContainer,
            action = MaterialTheme.colorScheme.error,
            actionContent = MaterialTheme.colorScheme.onError,
        )
    } else {
        SnackbarPalette(
            container = containerColor,
            content = contentColor,
            action = actionColor,
            actionContent = actionContentColor,
        )
    }

    SnackbarHost(
        modifier = modifier,
        hostState = hostState,
        snackbar = { data ->
            Snackbar(
                snackbarData = data,
                containerColor = palette.container,
                contentColor = palette.content,
                actionColor = palette.action,
                actionContentColor = palette.actionContent,
            )
        }
    )
}

private data class SnackbarPalette(
    val container: Color,
    val content: Color,
    val action: Color,
    val actionContent: Color,
)
