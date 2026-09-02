package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaDB
import io.github.juevigrace.diva.lib.database.user.actions.UserActionsStorage
import io.github.juevigrace.diva.lib.models.actions.Actions
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserActionsStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : UserActionsStorage {

    override suspend fun getById(id: Uuid): Result<Option<UserAction>> {
        return db.getOne {
            userActionsQueries.findOneById(id.toString(), ::mapToUserAction)
        }
    }

    override suspend fun getAllByUser(userId: Uuid): Result<List<UserAction>> {
        return db.getList {
            userActionsQueries.findAllByUser(userId.toString(), ::mapToUserAction)
        }
    }

    override suspend fun getByAction(userId: Uuid, action: Actions): Result<Option<UserAction>> {
        return db.getOne {
            userActionsQueries.findOneByAction(userId.toString(), action, ::mapToUserAction)
        }
    }

    override suspend fun upsert(item: UserAction): Result<Unit> {
        return db.use {
            transaction {
                userActionsQueries.upsert(
                    id = item.id.toString(),
                    name = item.action,
                    user_id = item.userId.toString()
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                userActionsQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userActionsQueries.deleteAll()
            }
        }
    }

    private fun mapToUserAction(
        id: String,
        name: Actions,
        userId: String,
    ): UserAction = UserAction(
        id = Uuid.parse(id),
        action = name,
        userId = Uuid.parse(userId)
    )
}
