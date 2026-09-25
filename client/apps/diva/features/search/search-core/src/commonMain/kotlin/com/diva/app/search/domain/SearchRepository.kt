package com.diva.app.search.domain

import io.github.juevigrace.diva.lib.core.Repository

interface SearchRepository : Repository {
    suspend fun search(query: String): Result<SearchResults>
}