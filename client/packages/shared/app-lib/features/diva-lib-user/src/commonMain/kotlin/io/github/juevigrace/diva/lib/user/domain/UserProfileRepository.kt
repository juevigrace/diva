package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository : Repository {
    val client: DivaClient

    fun getProfile(userId: String): Flow<Result<Option<UserProfile>>>

    suspend fun sync(userId: String): Result<Unit>

    suspend fun save(userId: String, profile: UserProfile): Result<Unit>
}
