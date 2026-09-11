package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrDefault
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.lib.database.user.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.data.api.client.UserProfileApi
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserProfileRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserProfileStorage,
    private val sessionRepository: SessionRepository,
    private val api: UserProfileApi,
) : UserProfileRepository {
    override fun getProfile(userId: Uuid): Flow<Result<Option<UserProfile>>> = storage.getByUserFlow(userId)

    override suspend fun sync(userId: Uuid): Result<Unit> {
        return withSession(
            sessionCall = sessionRepository::getCurrent,
            onFound = { session ->
                api.get(
                    uid = userId.toString(),
                    token = session.accessToken
                ).mapCatching { option ->
                    option.map { storage.upsert(userId, UserProfile.fromResponse(it)).getOrThrow() }
                        .getOrDefault(Unit)
                }
            },
        )
    }

    override suspend fun save(userId: Uuid, profile: UserProfile): Result<Unit> = storage.upsert(userId, profile)
}
