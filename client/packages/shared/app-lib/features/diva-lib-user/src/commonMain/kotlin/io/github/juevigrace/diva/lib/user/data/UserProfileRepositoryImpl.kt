package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserProfileRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserProfileStorage,
) : UserProfileRepository {
    override fun getProfile(userId: Uuid): Flow<Result<Option<UserProfile>>> = storage.getByUserFlow(userId)

    override suspend fun sync(userId: Uuid): Result<Unit> = Result.success(Unit)

    override suspend fun save(profile: UserProfile): Result<Unit> = storage.upsert(profile)
}
