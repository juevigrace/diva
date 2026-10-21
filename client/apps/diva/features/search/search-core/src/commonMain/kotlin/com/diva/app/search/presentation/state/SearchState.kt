package com.diva.app.search.presentation.state

import com.diva.app.search.models.SearchResults

data class SearchState(
    val query: String = "",
    /** Browse content shown while the query is empty, replaced live as the user types. */
    val defaults: SearchResults = SearchResults(),
    val results: SearchResults = SearchResults(),
    val suggestions: List<String> = emptyList(),
) {
    val isQueryEmpty: Boolean
        get() = query.isBlank()

    /** What the screen renders: query results while searching, browse defaults otherwise. */
    val displayed: SearchResults
        get() = if (isQueryEmpty) defaults else results

    val totalResults: Int
        get() = results.media.size + results.collections.size + results.folders.size
}
