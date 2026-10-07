package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserPreferencesApi
import io.github.juevigrace.diva.lib.user.database.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.lib.user.preferences.models.UserPreferences
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserPreferencesStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserPreferencesApi,
) : UserPreferencesRepository {
    override fun observe(): Flow<Result<UserPreferences>> = observeSession(sessionRepository::observe) { session ->
        storage.findOneFlow(session.userId).map { result ->
            result.mapCatching { option ->
                option.getOrThrow { IllegalStateException("No user preferences for user '${session.userId}'") }
            }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::get,
            onFound = { session ->
                api.getByUser(
                    uid = session.userId,
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(session.userId, UserPreferences.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun upsert(preferences: UserPreferences): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.upsert(session.userId, preferences)
    }

    override suspend fun delete(userId: String): Result<Unit> = storage.deleteOne(userId)
}
