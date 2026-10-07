package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.user.profile.models.UserProfile
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository : Repository {
    val client: DivaClient

    fun observe(): Flow<Result<UserProfile>>

    suspend fun sync(): Result<Unit>

    suspend fun upsert(profile: UserProfile): Result<Unit>
}
