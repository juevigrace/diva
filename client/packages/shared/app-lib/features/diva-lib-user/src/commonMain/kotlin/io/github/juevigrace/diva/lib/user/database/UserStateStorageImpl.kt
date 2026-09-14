package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import io.github.juevigrace.diva.lib.database.user.state.UserStateStorage
import io.github.juevigrace.diva.lib.models.user.UserStatus
import io.github.juevigrace.diva.lib.models.user.state.UserState
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserStateStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserStateStorage {

    override suspend fun getByUser(userId: Uuid): Result<Option<UserState>> {
        return db.getOne {
            userStateQueries.findByUser(userId.toString(), ::mapToUserState)
        }
    }

    override fun getByUserFlow(userId: Uuid): Flow<Result<Option<UserState>>> {
        return db.getOneAsFlow {
            userStateQueries.findByUser(userId.toString(), ::mapToUserState)
        }
    }

    override suspend fun upsert(userId: Uuid, item: UserState): Result<Unit> {
        return db.use {
            transaction {
                userStateQueries.upsert(
                    user_id = userId.toString(),
                    verified = item.verified,
                    status = item.status,
                    last_active_at = item.lastActiveAt.getOrNull() ?: 0L,
                    updated_at = item.updatedAt.getOrNull() ?: 0L
                )
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userStateQueries.deleteAll()
            }
        }
    }

    private fun mapToUserState(
        userId: String,
        verified: Boolean,
        status: UserStatus,
        lastActiveAt: Long,
        updatedAt: Long,
    ): UserState = UserState(
        verified = verified,
        status = status,
        lastActiveAt = lastActiveAt.toOption(),
        updatedAt = updatedAt.toOption()
    )
}
