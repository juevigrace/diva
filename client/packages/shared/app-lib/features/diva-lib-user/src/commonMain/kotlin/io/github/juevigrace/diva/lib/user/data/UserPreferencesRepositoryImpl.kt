package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.database.user.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserPreferencesApi
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserPreferencesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPreferencesStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserPreferencesApi,
) : UserPreferencesRepository {
    override fun getPreferences(userId: Uuid): Flow<Result<Option<UserPreferences>>> = storage.getByUserFlow(userId)

    override suspend fun sync(userId: Uuid): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.getByUser(
                    uid = userId.toString(),
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(userId, UserPreferences.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun save(
        userId: Uuid,
        preferences: UserPreferences
    ): Result<Unit> = storage.upsert(userId, preferences)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}
