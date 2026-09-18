package io.github.juevigrace.diva.lib.user.database.profile

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileStorage {
    suspend fun findOne(userId: String): Result<Option<UserProfile>>

    fun findOneFlow(userId: String): Flow<Result<Option<UserProfile>>>

    suspend fun upsert(userId: String, item: UserProfile): Result<Unit>

    suspend fun deleteOne(userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class UserProfileStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserProfileStorage {

    override suspend fun findOne(userId: String): Result<Option<UserProfile>> {
        return db.getOne {
            userProfilesQueries.findOne(userId, ::mapToUserProfile)
        }
    }

    override fun findOneFlow(userId: String): Flow<Result<Option<UserProfile>>> {
        return db.getOneAsFlow {
            userProfilesQueries.findOne(userId, ::mapToUserProfile)
        }
    }

    override suspend fun deleteOne(userId: String): Result<Unit> {
        return db.use {
            transaction {
                userProfilesQueries.deleteOne(userId)
            }
        }
    }

    override suspend fun upsert(userId: String, item: UserProfile): Result<Unit> {
        return db.use {
            transaction {
                userProfilesQueries.upsert(
                    user_id = userId,
                    first_name = item.firstName,
                    last_name = item.lastName,
                    birth_date = item.birthDate.getOrNull(),
                    alias = item.alias,
                    bio = item.bio,
                    avatar = item.avatar,
                    updated_at = item.updatedAt.getOrNull() ?: 0L
                )
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userProfilesQueries.delete()
            }
        }
    }

    private fun mapToUserProfile(
        userId: String,
        firstName: String,
        lastName: String,
        birthDate: Long?,
        alias: String,
        bio: String,
        avatar: String,
        updatedAt: Long,
    ): UserProfile = UserProfile(
        firstName = firstName,
        lastName = lastName,
        birthDate = birthDate.toOption(),
        alias = alias,
        bio = bio,
        avatar = avatar,
        updatedAt = updatedAt.toOption()
    )
}
