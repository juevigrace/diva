package com.diva.app.folder.presentation.viewmodel

import com.diva.app.folder.presentation.events.FolderEvents
import com.diva.app.folder.presentation.state.FolderState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FolderViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<FolderState>
        field = MutableStateFlow(FolderState())

    fun onEvent(event: FolderEvents) {
        when (event) {
            FolderEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}