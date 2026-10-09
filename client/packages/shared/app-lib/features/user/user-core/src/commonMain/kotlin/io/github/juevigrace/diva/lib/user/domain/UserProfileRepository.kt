package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.profile.models.UserProfile

interface UserProfileRepository : Repository {
    suspend fun get(id: String): Result<UserProfile>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(profile: UserProfile): Result<Unit>
}
