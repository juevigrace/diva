package com.diva.app.search.presentation.state

import com.diva.app.search.models.SearchResults

data class SearchState(
    val query: String = "",
    val results: SearchResults = SearchResults(),
    val suggestions: List<String> = emptyList(),
) {
    val isQueryEmpty: Boolean
        get() = query.isBlank()

    val totalResults: Int
        get() = results.media.size + results.collections.size + results.folders.size
}