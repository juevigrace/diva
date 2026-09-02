package io.github.juevigrace.diva.lib.user.database

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.database.DivaDatabase
import io.github.juevigrace.diva.lib.database.DivaDB
import io.github.juevigrace.diva.lib.database.user.preferences.UserPreferencesStorage
import io.github.juevigrace.diva.lib.models.Theme
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserPreferencesStorageImpl(
    private val db: DivaDatabase<DivaDB>
) : UserPreferencesStorage {

    override suspend fun getById(id: Uuid): Result<Option<UserPreferences>> {
        return db.getOne {
            userPreferencesQueries.findOneById(id.toString(), ::mapToUserPreferences)
        }
    }

    override suspend fun getByUser(userId: Uuid): Result<Option<UserPreferences>> {
        return db.getOne {
            userPreferencesQueries.findByUser(userId.toString(), ::mapToUserPreferences)
        }
    }

    override fun getByUserFlow(userId: Uuid): Flow<Result<Option<UserPreferences>>> {
        return db.getOneAsFlow {
            userPreferencesQueries.findByUser(userId.toString(), ::mapToUserPreferences)
        }
    }

    override suspend fun upsert(item: UserPreferences): Result<Unit> {
        return db.use {
            transaction {
                userPreferencesQueries.upsert(
                    id = item.id.toString(),
                    user_id = item.userId.toString(),
                    theme = item.theme,
                    onboarding_completed = item.onboardingCompleted,
                    language = item.language,
                    last_sync_at = item.lastSyncAt.map { it.epochSeconds }.getOrNull() ?: 0L,
                    created_at = item.createdAt.map { it.epochSeconds }.getOrNull() ?: 0L,
                    updated_at = item.updatedAt.map { it.epochSeconds }.getOrNull() ?: 0L
                )
            }
        }
    }

    override suspend fun delete(id: Uuid): Result<Unit> {
        return db.use {
            transaction {
                userPreferencesQueries.deleteById(id.toString())
            }
        }
    }

    override suspend fun deleteAll(): Result<Unit> {
        return db.use {
            transaction {
                userPreferencesQueries.deleteAll()
            }
        }
    }

    private fun mapToUserPreferences(
        id: String,
        userId: String,
        theme: Theme,
        onboardingCompleted: Boolean,
        language: String,
        lastSyncAt: Long,
        createdAt: Long,
        updatedAt: Long,
    ): UserPreferences = UserPreferences(
        id = Uuid.parse(id),
        userId = Uuid.parse(userId),
        theme = theme,
        onboardingCompleted = onboardingCompleted,
        language = language,
        lastSyncAt = Instant.fromEpochSeconds(lastSyncAt).toOption(),
        createdAt = Instant.fromEpochSeconds(createdAt).toOption(),
        updatedAt = Instant.fromEpochSeconds(updatedAt).toOption()
    )
}
