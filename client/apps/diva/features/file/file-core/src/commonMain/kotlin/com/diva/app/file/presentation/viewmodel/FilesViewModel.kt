package com.diva.app.file.presentation.viewmodel

import com.diva.app.file.domain.FileRepository
import com.diva.app.file.presentation.events.DirectoryEvents
import com.diva.app.file.presentation.state.DirectoryState
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilesViewModel(
    private val tabNavigator: TabNavigator,
    private val repository: FileRepository,
) : DivaViewModel() {
    val state: StateFlow<DirectoryState>
        field = MutableStateFlow(DirectoryState())

    fun onEvent(event: DirectoryEvents) {
        when (event) {
            is DirectoryEvents.OnOpenDirectory -> {}
            is DirectoryEvents.OnNavigateToBreadcrumb -> {}
        }
    }
}
