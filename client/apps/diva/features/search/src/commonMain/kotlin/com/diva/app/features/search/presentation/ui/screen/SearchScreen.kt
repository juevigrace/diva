package com.diva.app.features.search.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.app.features.search.presentation.events.SearchEvents
import com.diva.app.features.search.presentation.viewmodel.SearchViewModel
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.search
import io.github.juevigrace.diva.ui.layout.Screen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Screen(
        topBar = {
            OutlinedTextField(
                value = state.value.query,
                onValueChange = { query -> viewModel.onEvent(SearchEvents.OnQueryChange(query)) },
                placeholder = { Text(text = stringResource(Res.string.search)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        },
    ) { innerPadding ->
        val results = state.value.results
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            if (results.media.isNotEmpty()) {
                item { SectionHeader(title = "Media") }
                items(results.media, key = { it.id }) { item ->
                    ResultRow(title = item.title)
                }
            }
            if (results.folders.isNotEmpty()) {
                item { SectionHeader(title = "Folders") }
                items(results.folders, key = { it.id }) { item ->
                    ResultRow(title = item.name)
                }
            }
            if (results.collections.isNotEmpty()) {
                item { SectionHeader(title = "Collections") }
                items(results.collections, key = { it.id }) { item ->
                    ResultRow(title = item.name)
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
private fun ResultRow(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
    )
}