package io.github.juevigrace.diva.lib.user.database.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.user.UserStatus
import io.github.juevigrace.diva.lib.models.user.state.UserState
import kotlinx.coroutines.flow.Flow

interface UserStateStorage {
    suspend fun findOne(userId: String): Result<Option<UserState>>

    fun findOneFlow(userId: String): Flow<Result<Option<UserState>>>

    suspend fun upsert(userId: String, item: UserState): Result<Unit>

    suspend fun deleteOne(userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class UserStateStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserStateStorage {

    override suspend fun findOne(userId: String): Result<Option<UserState>> {
        return db.getOne {
            userStateQueries.findOne(userId, ::mapToUserState)
        }
    }

    override fun findOneFlow(userId: String): Flow<Result<Option<UserState>>> {
        return db.getOneAsFlow {
            userStateQueries.findOne(userId, ::mapToUserState)
        }
    }

    override suspend fun deleteOne(userId: String): Result<Unit> {
        return db.use {
            transaction {
                userStateQueries.deleteOne(userId)
            }
        }
    }

    override suspend fun upsert(userId: String, item: UserState): Result<Unit> {
        return db.use {
            transaction {
                userStateQueries.upsert(
                    user_id = userId,
                    verified = item.verified,
                    status = item.status,
                    last_active_at = item.lastActiveAt.getOrNull() ?: 0L,
                    updated_at = item.updatedAt.getOrNull() ?: 0L
                )
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userStateQueries.delete()
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
