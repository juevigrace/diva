package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaDB
import io.github.juevigrace.diva.lib.database.user.UserStorage
import io.github.juevigrace.diva.lib.models.user.Role
import io.github.juevigrace.diva.lib.models.user.User
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : UserStorage {

    override suspend fun getAll(): Result<List<User>> {
        return db.getList {
            userQueries.findAll(::mapToUser)
        }
    }

    override fun getAllFlow(): Flow<Result<List<User>>> {
        return db.getListAsFlow {
            userQueries.findAll(::mapToUser)
        }
    }

    override suspend fun getById(id: Uuid): Result<Option<User>> {
        return db.getOne {
            userQueries.findOneById(id.toString(), ::mapToUser)
        }
    }

    override fun getByIdFlow(id: Uuid): Flow<Result<Option<User>>> {
        return db.getOneAsFlow {
            userQueries.findOneById(id.toString(), ::mapToUser)
        }
    }

    override suspend fun upsert(item: User): Result<Unit> {
        return db.use {
            transaction {
                userQueries.upsert(
                    id = item.id.toString(),
                    username = item.username,
                    email = item.email,
                    phone_number = item.phoneNumber,
                    password_hash = item.passwordHash.getOrNull() ?: "",
                    role = item.role,
                    created_at = item.createdAt.epochSeconds,
                    updated_at = item.updatedAt.epochSeconds
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                userQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userQueries.deleteAll()
            }
        }
    }

    private fun mapToUser(
        id: String,
        username: String,
        email: String,
        phoneNumber: String,
        passwordHash: String,
        role: Role,
        createdAt: Long,
        updatedAt: Long,
    ): User {
        return User(
            id = Uuid.parse(id),
            username = username,
            email = email,
            phoneNumber = phoneNumber,
            passwordHash = passwordHash.toOption(),
            role = role,
            createdAt = Instant.fromEpochSeconds(createdAt),
            updatedAt = Instant.fromEpochSeconds(updatedAt)
        )
    }
}
