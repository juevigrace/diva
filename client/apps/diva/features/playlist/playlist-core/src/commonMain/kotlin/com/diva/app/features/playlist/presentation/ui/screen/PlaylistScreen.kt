package com.diva.app.features.playlist.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.features.playlist.presentation.events.PlaylistEvents
import com.diva.app.features.playlist.presentation.viewmodel.PlaylistViewModel
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.BackHandler
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler { viewModel.onEvent(PlaylistEvents.OnBack) }

    Screen { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Your playlists.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}