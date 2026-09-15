package io.github.juevigrace.diva.lib.user.domain

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.Repository
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository : Repository {
    val client: DivaClient

    fun getPreferences(userId: String): Flow<Result<Option<UserPreferences>>>

    suspend fun sync(userId: String): Result<Unit>

    suspend fun save(userId: String, preferences: UserPreferences): Result<Unit>

    suspend fun delete(id: String): Result<Unit>
}
