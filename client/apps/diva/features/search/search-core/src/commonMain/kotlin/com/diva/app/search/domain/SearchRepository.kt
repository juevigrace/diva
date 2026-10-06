package com.diva.app.search.domain

import com.diva.app.search.models.SearchResults
import io.github.juevigrace.diva.lib.core.Repository

interface SearchRepository : Repository {
    suspend fun search(query: String): Result<SearchResults>
}