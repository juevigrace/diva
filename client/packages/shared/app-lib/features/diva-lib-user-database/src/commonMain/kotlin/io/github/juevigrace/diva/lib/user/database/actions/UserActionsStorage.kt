package io.github.juevigrace.diva.lib.user.database.actions

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import kotlinx.coroutines.flow.Flow

interface UserActionsStorage {
    suspend fun findAll(userId: String): Result<List<UserAction>>

    fun findAllFlow(userId: String): Flow<Result<List<UserAction>>>

    suspend fun findOne(id: String): Result<Option<UserAction>>

    fun findOneFlow(id: String): Flow<Result<Option<UserAction>>>

    suspend fun findByAction(userId: String, action: Actions): Result<Option<UserAction>>

    fun findByActionFlow(userId: String, action: Actions): Flow<Result<Option<UserAction>>>

    suspend fun upsert(userId: String, item: UserAction): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class UserActionsStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserActionsStorage {

    override suspend fun findOne(id: String): Result<Option<UserAction>> {
        return db.getOne {
            userActionsQueries.findOne(id, ::mapToUserAction)
        }
    }

    override fun findOneFlow(id: String): Flow<Result<Option<UserAction>>> {
        return db.getOneAsFlow {
            userActionsQueries.findOne(id, ::mapToUserAction)
        }
    }

    override suspend fun findAll(userId: String): Result<List<UserAction>> {
        return db.getList {
            userActionsQueries.findAll(userId, ::mapToUserAction)
        }
    }

    override fun findAllFlow(userId: String): Flow<Result<List<UserAction>>> {
        return db.getListAsFlow {
            userActionsQueries.findAll(userId, ::mapToUserAction)
        }
    }

    override suspend fun findByAction(userId: String, action: Actions): Result<Option<UserAction>> {
        return db.getOne {
            userActionsQueries.findOneByAction(userId, action, ::mapToUserAction)
        }
    }

    override fun findByActionFlow(userId: String, action: Actions): Flow<Result<Option<UserAction>>> {
        return db.getOneAsFlow {
            userActionsQueries.findOneByAction(userId, action, ::mapToUserAction)
        }
    }

    override suspend fun upsert(userId: String, item: UserAction): Result<Unit> {
        return db.use {
            transaction {
                userActionsQueries.upsert(
                    id = item.id,
                    name = item.action,
                    user_id = userId
                )
            }
        }
    }

    override suspend fun deleteOne(id: String): Result<Unit> {
        return db.use {
            transaction {
                userActionsQueries.deleteOne(id)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userActionsQueries.delete()
            }
        }
    }

    private fun mapToUserAction(
        id: String,
        name: Actions,
        userId: String,
    ): UserAction = UserAction(
        id = id,
        action = name
    )
}