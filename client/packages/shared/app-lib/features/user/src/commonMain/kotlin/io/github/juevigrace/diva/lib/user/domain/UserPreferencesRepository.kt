package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface UserPreferencesRepository : Repository {
    val client: DivaClient

    fun getPreferences(userId: Uuid): Flow<Result<Option<UserPreferences>>>

    suspend fun sync(userId: Uuid): Result<Unit>

    suspend fun save(preferences: UserPreferences): Result<Unit>

    suspend fun delete(id: Uuid): Result<Unit>
}
