package com.diva.app.profile.domain

import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface ProfileRepository : Repository {
    fun observeProfile(): Flow<Result<Profile?>>
}