package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserProfileRepository : Repository {
    val client: DivaClient

    fun getProfile(userId: Uuid): Flow<Result<Option<UserProfile>>>

    suspend fun sync(userId: Uuid): Result<Unit>

    suspend fun save(profile: UserProfile): Result<Unit>
}
