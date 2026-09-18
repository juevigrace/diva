package io.github.juevigrace.diva.lib.user.database.preferences

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.user.DivaSharedDB
import io.github.juevigrace.diva.lib.models.Theme
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesStorage {
    suspend fun findOne(userId: String): Result<Option<UserPreferences>>

    fun findOneFlow(userId: String): Flow<Result<Option<UserPreferences>>>

    suspend fun upsert(userId: String, item: UserPreferences): Result<Unit>

    suspend fun deleteOne(userId: String): Result<Unit>

    suspend fun delete(): Result<Unit>
}

class UserPreferencesStorageImpl(
    private val db: DivaDatabase<DivaSharedDB>
) : UserPreferencesStorage {

    override suspend fun findOne(userId: String): Result<Option<UserPreferences>> {
        return db.getOne {
            userPreferencesQueries.findOne(userId, ::mapToUserPreferences)
        }
    }

    override fun findOneFlow(userId: String): Flow<Result<Option<UserPreferences>>> {
        return db.getOneAsFlow {
            userPreferencesQueries.findOne(userId, ::mapToUserPreferences)
        }
    }

    override suspend fun upsert(userId: String, item: UserPreferences): Result<Unit> {
        return db.use {
            transaction {
                userPreferencesQueries.upsert(
                    user_id = userId,
                    theme = item.theme,
                    onboarding_completed = item.onboardingCompleted,
                    language = item.language,
                    last_sync_at = item.lastSyncAt.getOrNull() ?: 0L,
                    created_at = item.createdAt.getOrNull() ?: 0L,
                    updated_at = item.updatedAt.getOrNull() ?: 0L
                )
            }
        }
    }

    override suspend fun deleteOne(userId: String): Result<Unit> {
        return db.use {
            transaction {
                userPreferencesQueries.deleteOne(userId)
            }
        }
    }

    override suspend fun delete(): Result<Unit> {
        return db.use {
            transaction {
                userPreferencesQueries.delete()
            }
        }
    }

    private fun mapToUserPreferences(
        userId: String,
        theme: Theme,
        onboardingCompleted: Boolean,
        language: String,
        lastSyncAt: Long,
        createdAt: Long,
        updatedAt: Long,
    ): UserPreferences = UserPreferences(
        theme = theme,
        onboardingCompleted = onboardingCompleted,
        language = language,
        lastSyncAt = lastSyncAt.toOption(),
        createdAt = createdAt.toOption(),
        updatedAt = updatedAt.toOption()
    )
}
