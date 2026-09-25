package com.diva.app.library.presentation.viewmodel

import com.diva.app.library.presentation.events.LibraryEvents
import com.diva.app.library.presentation.state.LibraryState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LibraryViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<LibraryState>
        field = MutableStateFlow(LibraryState())

    fun onEvent(event: LibraryEvents) {
        when (event) {
            LibraryEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}