package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserProfileApi
import io.github.juevigrace.diva.lib.user.database.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.lib.user.profile.models.UserProfile
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserProfileRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserProfileStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserProfileApi,
) : UserProfileRepository {
    override fun observe(): Flow<Result<UserProfile>> = observeSession(sessionRepository::observe) { session ->
        storage.findOneFlow(session.userId).map { result ->
            result.mapCatching { option ->
                option.getOrThrow { IllegalStateException("No user profile for user '${session.userId}'") }
            }
        }
    }

    override suspend fun sync(): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::get,
            onFound = { session ->
                api.get(
                    uid = session.userId,
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(session.userId, UserProfile.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun upsert(profile: UserProfile): Result<Unit> = withSession(sessionRepository::get) { session ->
        storage.upsert(session.userId, profile)
    }
}
