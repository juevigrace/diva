package com.diva.app.search.presentation.viewmodel

import com.diva.app.search.domain.SearchRepository
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import com.diva.app.search.presentation.state.searchMock
import com.diva.app.search.presentation.state.searchSuggestions
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel(
    private val repository: SearchRepository,
    private val navigator: Navigator,
) : DivaViewModel() {

    // TODO(ui-pass): restore repository wiring
    val state: StateFlow<SearchState>
        field = MutableStateFlow(SearchState(suggestions = searchSuggestions()))

    fun onEvent(event: SearchEvents) {
        when (event) {
            is SearchEvents.OnQueryChange -> updateQuery(event.query)
            is SearchEvents.OnSelectSuggestion -> updateQuery(event.suggestion)
            SearchEvents.OnClear -> updateQuery("")
            SearchEvents.OnBack -> onBack()
        }
    }

    private fun updateQuery(query: String) {
        state.update { current ->
            current.copy(
                query = query,
                results = searchMock(query),
            )
        }
    }

    private fun onBack() {
        navigator.pop()
    }
}