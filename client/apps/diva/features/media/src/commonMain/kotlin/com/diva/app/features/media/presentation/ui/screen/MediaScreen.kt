package com.diva.app.features.media.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.features.media.presentation.viewmodel.MediaViewModel
import io.github.juevigrace.diva.ui.layout.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MediaScreen(
    viewModel: MediaViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Screen { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = state.value.title)
        }
    }
}