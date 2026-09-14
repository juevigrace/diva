package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaSharedDB
import io.github.juevigrace.diva.lib.database.user.profile.UserProfileStorage
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserProfileStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserProfileStorage {

    override suspend fun getByUser(userId: Uuid): Result<Option<UserProfile>> {
        return db.getOne {
            userProfilesQueries.findByUser(userId.toString(), ::mapToUserProfile)
        }
    }

    override fun getByUserFlow(userId: Uuid): Flow<Result<Option<UserProfile>>> {
        return db.getOneAsFlow {
            userProfilesQueries.findByUser(userId.toString(), ::mapToUserProfile)
        }
    }

    override suspend fun upsert(userId: Uuid, item: UserProfile): Result<Unit> {
        return db.use {
            transaction {
                userProfilesQueries.upsert(
                    user_id = userId.toString(),
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

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userProfilesQueries.deleteAll()
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
