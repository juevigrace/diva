package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserPreferencesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPreferencesStorage,
) : UserPreferencesRepository {
    override fun getPreferences(userId: Uuid): Flow<Result<Option<UserPreferences>>> = storage.getByUserFlow(userId)

    override suspend fun sync(userId: Uuid): Result<Unit> = Result.success(Unit)

    override suspend fun save(preferences: UserPreferences): Result<Unit> = storage.upsert(preferences)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}
