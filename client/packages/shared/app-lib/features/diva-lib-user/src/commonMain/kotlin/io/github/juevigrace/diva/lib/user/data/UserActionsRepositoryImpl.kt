package io.github.juevigrace.diva.lib.user.data

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.database.user.actions.UserActionsStorage
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import io.github.juevigrace.diva.lib.user.domain.UserActionsRepository
import io.github.juevigrace.diva.network.client.DivaClient
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserActionsRepositoryImpl(
    override val client: DivaClient,
    private val storage: UserActionsStorage,
) : UserActionsRepository {
    override fun getActions(userId: Uuid): Flow<Result<List<UserAction>>> = storage.getAllByUserFlow(userId)

    override fun getAction(userId: Uuid, action: Actions): Flow<Result<Option<UserAction>>> =
        storage.getByActionFlow(userId, action)

    override suspend fun sync(userId: Uuid): Result<Unit> = Result.success(Unit)

    override suspend fun save(action: UserAction): Result<Unit> = storage.upsert(action)

    override suspend fun delete(id: Uuid): Result<Unit> = storage.delete(id)
}
