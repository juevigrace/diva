package com.diva.app.search.presentation.state

import com.diva.app.search.models.SearchResults

data class SearchState(
    val query: String = "",
    val results: SearchResults = SearchResults(),
)