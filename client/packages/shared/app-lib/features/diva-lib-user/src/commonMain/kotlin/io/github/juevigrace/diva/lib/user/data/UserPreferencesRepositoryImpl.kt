package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.user.database.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserPreferencesApi
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPreferencesStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserPreferencesApi,
) : UserPreferencesRepository {
    override fun getPreferences(userId: String): Flow<Result<Option<UserPreferences>>> = storage.findOneFlow(userId)

    override suspend fun sync(userId: String): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.getByUser(
                    uid = userId,
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(userId, UserPreferences.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun save(
        userId: String,
        preferences: UserPreferences
    ): Result<Unit> = storage.upsert(userId, preferences)

    override suspend fun delete(id: String): Result<Unit> = storage.deleteOne(id)
}
