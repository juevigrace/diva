package com.diva.app.search.domain

import io.github.juevigrace.diva.lib.core.Repository
import com.diva.app.search.models.SearchResults

interface SearchRepository : Repository {
    suspend fun search(query: String): Result<SearchResults>
}