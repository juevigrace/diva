package io.github.juevigrace.diva.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Stable
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
    loading: @Composable () -> Unit = { DefaultLoading(modifier) },
    error: @Composable (message: String, retry: () -> Unit) -> Unit = { message, retry ->
        DefaultError(message, retry, onRetry)
    },
    empty: @Composable () -> Unit = { DefaultEmpty(modifier) },
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
private fun DefaultLoading(modifier: Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DefaultEmpty(modifier: Modifier) {
    Column(
        modifier = Modifier.fillMaxSize().then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Nothing here yet",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun DefaultError(message: String, retry: () -> Unit, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(onClick = { retry() }) {
            Text("Retry")
        }
    }
}
