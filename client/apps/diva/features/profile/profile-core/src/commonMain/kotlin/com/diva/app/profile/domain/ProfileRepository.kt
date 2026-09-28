package com.diva.app.profile.domain

import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow
import com.diva.app.profile.models.Profile

interface ProfileRepository : Repository {
    fun observeProfile(): Flow<Result<Profile?>>
}