package com.diva.app.collection.presentation.viewmodel

import com.diva.app.collection.presentation.events.CollectionEvents
import com.diva.app.collection.presentation.state.CollectionState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CollectionViewModel(
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<CollectionState>
        field = MutableStateFlow(CollectionState())

    fun onEvent(event: CollectionEvents) {
        when (event) {
            CollectionEvents.OnBack -> onBack()
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}