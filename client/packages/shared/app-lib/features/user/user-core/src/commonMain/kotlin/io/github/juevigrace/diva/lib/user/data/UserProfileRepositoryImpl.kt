package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.getOrThrow
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.withSession
import io.github.juevigrace.diva.lib.user.data.api.client.UserProfileApi
import io.github.juevigrace.diva.lib.user.database.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.lib.user.profile.models.UserProfile

class UserProfileRepositoryImpl(
    private val storage: UserProfileStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserProfileApi,
) : UserProfileRepository {
    override suspend fun get(id: String): Result<UserProfile> = storage.findOne(id).mapCatching { option ->
        option.getOrThrow { IllegalStateException("No user profile for user '$id'") }
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
