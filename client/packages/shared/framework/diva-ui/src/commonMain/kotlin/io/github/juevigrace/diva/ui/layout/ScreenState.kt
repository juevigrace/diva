package io.github.juevigrace.diva.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

sealed interface ScreenState<out T> {
    data object Loading : ScreenState<Nothing>

    data class Error(val message: String) : ScreenState<Nothing>

    data object Empty : ScreenState<Nothing>

    data class Content<T>(val value: T) : ScreenState<T>
}

@Composable
fun <T> StateLayout(
    state: ScreenState<T>,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {},
    loading: @Composable () -> Unit = { LoadingContent(modifier = modifier) },
    error: @Composable (message: String, retry: () -> Unit) -> Unit = { message, retry ->
        ErrorContent(message, modifier = modifier, onRetry = retry)
    },
    empty: @Composable () -> Unit = { EmptyContent(modifier = modifier) },
    content: @Composable (value: T) -> Unit,
) {
    when (state) {
        is ScreenState.Loading -> loading()
        is ScreenState.Error -> error(state.message) { onRetry() }
        is ScreenState.Empty -> empty()
        is ScreenState.Content -> content(state.value)
    }
}

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable ColumnScope.() -> Unit = {
        CircularProgressIndicator()
    },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content,
    )
}

@Composable
fun EmptyContent(
    text: String = "Nothing here yet",
    modifier: Modifier = Modifier.fillMaxSize(),
    content: @Composable ColumnScope.() -> Unit = {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content,
    )
}

@Composable
fun ErrorContent(
    message: String,
    retryText: String = "Retry",
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxSize().padding(16.dp),
    content: @Composable ColumnScope.() -> Unit = {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(onClick = onRetry) {
            Text(retryText)
        }
    },
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content,
    )
}
