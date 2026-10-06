package com.diva.app.profile.domain

import com.diva.app.profile.models.Profile
import io.github.juevigrace.diva.lib.core.Repository
import kotlinx.coroutines.flow.Flow

interface ProfileRepository : Repository {
    fun observeProfile(): Flow<Result<Profile?>>
}