package com.diva.app.features.search.presentation.state

import com.diva.app.features.search.domain.SearchResults

data class SearchState(
    val query: String = "",
    val results: SearchResults = SearchResults(),
)