package com.diva.app.file.presentation.viewmodel

import com.diva.app.file.domain.FileRepository
import com.diva.app.file.models.Directory
import com.diva.app.file.presentation.events.DirectoryEvents
import com.diva.app.file.presentation.state.DirectoryState
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilesViewModel(
    private val repository: FileRepository,
) : DivaViewModel() {
    val state: StateFlow<DirectoryState>
        field = MutableStateFlow(DirectoryState())

    init {
        scope.launch {
            state
                .map { it.breadcrumbs.lastOrNull()?.id }
                .distinctUntilChanged()
                .collectLatest { id ->
                    val flow = if (id == null) repository.observeRoot() else repository.observeDir(id)
                    flow.collect { res ->
                        res.fold(
                            onFailure = {
                                // TODO: handle error
                            },
                            onSuccess = { list ->
                                state.update { state ->
                                    state.copy(entries = list)
                                }
                            },
                        )
                    }
                }
        }
    }

    fun onEvent(event: DirectoryEvents) {
        when (event) {
            is DirectoryEvents.OnOpenDirectory -> openDirectory(event.directoryId)
            is DirectoryEvents.OnNavigateToBreadcrumb -> navigateTo(event.directoryId)
            DirectoryEvents.OnNavigateUp -> navigateUp()
        }
    }

    private fun openDirectory(directoryId: String) {
        val directory = state.value.entries
            .filterIsInstance<Directory>()
            .firstOrNull { it.id == directoryId } ?: return
        state.update { state ->
            state.copy(breadcrumbs = state.breadcrumbs + directory)
        }
    }

    private fun navigateTo(directoryId: String) {
        state.update { state ->
            val index = state.breadcrumbs.indexOfFirst { it.id == directoryId }
            if (index < 0) state else state.copy(breadcrumbs = state.breadcrumbs.take(index + 1))
        }
    }

    private fun navigateUp() {
        state.update { state ->
            if (state.breadcrumbs.isEmpty()) state else state.copy(breadcrumbs = state.breadcrumbs.dropLast(1))
        }
    }
}
