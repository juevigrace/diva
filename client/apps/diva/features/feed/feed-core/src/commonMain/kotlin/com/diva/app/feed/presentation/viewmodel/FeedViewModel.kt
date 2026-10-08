package com.diva.app.feed.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diva.app.feed.domain.FeedRepository
import com.diva.app.feed.presentation.events.FeedEvents
import com.diva.app.feed.presentation.state.FeedState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FeedViewModel(
    private val repository: FeedRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state: StateFlow<FeedState> = _state.asStateFlow()

    fun onEvent(event: FeedEvents) {
        when (event) {
            FeedEvents.Refresh -> loadFeed()
            is FeedEvents.OnSelectMedia -> { /* Scaffold: To be implemented */ }
        }
    }

    private fun loadFeed() {
        viewModelScope.launch {
            // Scaffold: To be implemented
        }
    }
}
