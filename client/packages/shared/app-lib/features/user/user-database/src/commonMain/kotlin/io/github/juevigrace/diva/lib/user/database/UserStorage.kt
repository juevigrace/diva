package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.Role
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow

interface UserStorage {
    suspend fun findAll(): Result<List<User>>

    fun findAllFlow(): Flow<Result<List<User>>>

    suspend fun findOne(id: String): Result<Option<User>>

    fun findOneFlow(id: String): Flow<Result<Option<User>>>

    suspend fun upsert(item: User): Result<Unit>

    suspend fun deleteOne(id: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class UserStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>,
) : UserStorage {

    override suspend fun findAll(): Result<List<User>> {
        return db.getList {
            userQueries.findAll(::mapToUser)
        }
    }

    override fun findAllFlow(): Flow<Result<List<User>>> {
        return db.getListAsFlow {
            userQueries.findAll(::mapToUser)
        }
    }

    override suspend fun findOne(id: String): Result<Option<User>> {
        return db.getOne {
            userQueries.findOne(id, ::mapToUser)
        }
    }

    override fun findOneFlow(id: String): Flow<Result<Option<User>>> {
        return db.getOneAsFlow {
            userQueries.findOne(id, ::mapToUser)
        }
    }

    override suspend fun upsert(item: User): Result<Unit> {
        return db.use {
            transaction {
                userQueries.upsert(
                    id = item.id,
                    username = item.username,
                    email = item.email.getOrNull() ?: "",
                    phone_number = item.phoneNumber.getOrNull() ?: "",
                    password_hash = item.passwordHash.getOrNull() ?: "",
                    role = item.role,
                    created_at = item.createdAt,
                    updated_at = item.updatedAt
                )
            }
        }
    }

    override suspend fun deleteOne(id: String): Result<Unit> {
        return db.use {
            transaction {
                userQueries.deleteOne(id)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userQueries.delete()
            }
        }
    }

    @Suppress("LongParameterList")
    private fun mapToUser(
        id: String,
        username: String,
        email: String?,
        phoneNumber: String?,
        passwordHash: String?,
        role: Role,
        createdAt: Long,
        updatedAt: Long,
        deletedAt: Long?,
    ): User {
        return User(
            id = id,
            username = username,
            email = email.toOption(),
            phoneNumber = phoneNumber.toOption(),
            passwordHash = passwordHash.toOption(),
            role = role,
            createdAt = createdAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt.toOption()
        )
    }
}
