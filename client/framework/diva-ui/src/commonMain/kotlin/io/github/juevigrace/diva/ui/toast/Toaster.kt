package io.github.juevigrace.diva.ui.toast

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.onSome
import io.github.juevigrace.diva.ui.util.ObserveFlow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

data class ToastMessage(
    val message: String,
    val actionLabel: Option<StringResource> = Option.None,
    val withDismissAction: Boolean = true,
    val duration: SnackbarDuration = if (actionLabel is Option.None) {
        SnackbarDuration.Short
    } else {
        SnackbarDuration.Indefinite
    },
    val isError: Boolean = false,
    val details: Option<String> = Option.None,
)

class Toaster {
    private val _messages: Channel<ToastMessage> = Channel(capacity = Channel.UNLIMITED)

    val messages: Flow<ToastMessage>
        get() = _messages.receiveAsFlow()

    suspend fun show(message: ToastMessage) {
        _messages.send(message)
    }
}

val LocalToaster: ProvidableCompositionLocal<Toaster> = staticCompositionLocalOf { Toaster() }

@Composable
fun rememberToaster(): Toaster {
    return remember { Toaster() }
}

@Composable
fun ToasterHost(
    modifier: Modifier = Modifier,
    toaster: Toaster = LocalToaster.current,
    hostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scope = rememberCoroutineScope()
    var currentIsError by remember { mutableStateOf(false) }

    ObserveFlow(flow = toaster.messages) { message ->
        currentIsError = message.isError
        scope.launch {
            hostState.showSnackbar(
                message = buildString {
                    append(message.message)
                    message.details.onSome { details -> append(" - $details") }
                },
                actionLabel = message.actionLabel.map { getString(it) }.getOrElse { null },
                withDismissAction = message.withDismissAction,
                duration = message.duration,
            )
        }
    }

    SnackbarHost(
        modifier = modifier,
        hostState = hostState,
        snackbar = { data ->
            val palette = snackbarPalette(isError = currentIsError)
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

private data class ToasterPalette(
    val container: Color,
    val content: Color,
    val action: Color,
    val actionContent: Color,
)

@Composable
private fun snackbarPalette(isError: Boolean): ToasterPalette {
    return if (isError) {
        ToasterPalette(
            container = MaterialTheme.colorScheme.errorContainer,
            content = MaterialTheme.colorScheme.onErrorContainer,
            action = MaterialTheme.colorScheme.error,
            actionContent = MaterialTheme.colorScheme.onError,
        )
    } else {
        ToasterPalette(
            container = SnackbarDefaults.color,
            content = SnackbarDefaults.contentColor,
            action = SnackbarDefaults.actionColor,
            actionContent = SnackbarDefaults.actionContentColor,
        )
    }
}
