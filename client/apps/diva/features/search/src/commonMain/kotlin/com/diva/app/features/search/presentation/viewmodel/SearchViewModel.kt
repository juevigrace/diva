package com.diva.app.features.search.presentation.viewmodel

import com.diva.app.features.search.domain.SearchRepository
import com.diva.app.features.search.domain.SearchResults
import com.diva.app.features.search.presentation.events.SearchEvents
import com.diva.app.features.search.presentation.state.SearchState
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository,
    private val navigator: Navigator,
) : DivaViewModel() {
    val state: StateFlow<SearchState>
        field = MutableStateFlow(SearchState())

    fun onEvent(event: SearchEvents) {
        when (event) {
            is SearchEvents.OnQueryChange -> updateQuery(event.query)
            SearchEvents.OnClear -> updateQuery("")
            SearchEvents.OnBack -> onBack()
        }
    }

    private fun updateQuery(query: String) {
        state.update { it.copy(query = query) }
        scope.launch {
            repository.search(query).fold(
                onSuccess = { results ->
                    state.update { it.copy(results = results) }
                },
                onFailure = {
                    state.update { it.copy(results = SearchResults()) }
                },
            )
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}