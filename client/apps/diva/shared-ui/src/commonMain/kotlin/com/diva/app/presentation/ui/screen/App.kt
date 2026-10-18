package com.diva.app.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.presentation.ui.theme.AppTypography
import com.diva.app.presentation.ui.theme.darkScheme
import com.diva.app.presentation.ui.theme.lightScheme
import com.diva.app.presentation.viewmodel.SystemUiState
import com.diva.app.presentation.viewmodel.SystemViewModel
import io.github.juevigrace.diva.ui.theme.DivaTheme
import io.github.juevigrace.diva.ui.theme.DivaThemeConfig
import io.github.juevigrace.diva.ui.theme.ThemeScheme
import org.koin.compose.viewmodel.koinViewModel

private const val PROBE_URL = "https://example.com"

@Composable
fun App() {
    val viewModel: SystemViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    DivaTheme(
        config = DivaThemeConfig(
            themeScheme = ThemeScheme(
                light = lightScheme,
                dark = darkScheme,
            ),
            typography = AppTypography,
        ),
    ) {
        SystemScreen(
            state = state,
            onRefresh = viewModel::refresh,
            onFetch = viewModel::fetch,
        )
    }
}

@Composable
private fun SystemScreen(
    state: SystemUiState,
    onRefresh: () -> Unit,
    onFetch: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "Diva", style = MaterialTheme.typography.headlineMedium)

        Text(text = "Rust core version: ${state.version}")

        state.info?.let { info ->
            Text(text = "OS: ${info.os} (${info.arch})")
            Text(text = "Rust toolchain: ${info.rustVersion}")
        }

        Text(text = "SQLite probe: ${state.databaseProbe}")

        if (state.httpResult.isNotBlank()) {
            Text(text = "HTTP probe length: ${state.httpResult.length}")
        }

        state.error?.let { error ->
            Text(text = "Error: $error", color = MaterialTheme.colorScheme.error)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onRefresh) { Text("Refresh") }
            Button(onClick = { onFetch(PROBE_URL) }) { Text("Fetch example.com") }
        }
    }
}
