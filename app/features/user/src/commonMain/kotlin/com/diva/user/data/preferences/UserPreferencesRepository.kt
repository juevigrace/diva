package com.diva.user.data.preferences

import com.diva.auth.session.data.SessionRepository
import com.diva.database.user.preferences.UserPreferencesStorage
import com.diva.models.Repository
import com.diva.models.api.user.preferences.dtos.CreateUserPreferencesDto
import com.diva.models.api.user.preferences.dtos.UpdateUserPreferencesDto
import com.diva.models.user.preferences.UserPreferences
import com.diva.user.api.client.preferences.UserPreferencesApi
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.ConstraintViolationException
import io.github.juevigrace.diva.core.fold
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.fold
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserPreferencesRepository : Repository {
    suspend fun getLocalPreferences(): Result<UserPreferences>

    fun getUserPreferences(): Flow<Result<UserPreferences>>

    suspend fun createCloudPreferences(prefs: UserPreferences): Result<Unit>

    suspend fun updateCloudPreferences(prefs: UserPreferences): Result<Unit>

    suspend fun updatePreferences(prefs: UserPreferences): Result<Unit>
}

class UserPreferencesRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val storage: UserPreferencesStorage,
    private val client: UserPreferencesApi,
) : UserPreferencesRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getLocalPreferences(): Result<UserPreferences> {
        return storage.getLocal().fold(
            onFailure = { err -> Result.failure(err) },
            onSuccess = { opt ->
                opt.fold(
                    onNone = {
                        val prefs = UserPreferences(id = Uuid.random())
                        storage.upsert(prefs).onFailure { err ->
                            return@fold Result.failure(err)
                        }
                        return@fold getLocalPreferences()
                    },
                    onSome = { prefs -> Result.success(prefs) }
                )
            }
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun getUserPreferences(): Flow<Result<UserPreferences>> {
        return flow {
            withSession(sessionRepository::getCurrent) { session ->
                client.getByUser(session.user.id.toString(), session.accessToken).fold(
                    onFailure = { err -> Result.failure(err) },
                    onSuccess = { response ->
                        if (response == null) {
                            val prefs = UserPreferences(id = Uuid.random(), onboardingCompleted = true)
                            storage.upsert(prefs).fold(
                                onFailure = { err -> Result.failure(err) },
                                onSuccess = {
                                    storage.updateUserId(prefs.id, session.user.id).fold(
                                        onFailure = { err ->
                                            if (err is ConstraintViolationException) {
                                                createCloudPreferences(prefs).fold(
                                                    onFailure = { e -> Result.failure(e) },
                                                    onSuccess = { Result.success(prefs) }
                                                )
                                            } else {
                                                Result.failure(err)
                                            }
                                        },
                                        onSuccess = {
                                            createCloudPreferences(prefs).fold(
                                                onFailure = { e -> Result.failure(e) },
                                                onSuccess = { Result.success(prefs) }
                                            )
                                        }
                                    )
                                }
                            )
                        } else {
                            val prefs = UserPreferences.fromResponse(response)
                            storage.upsert(prefs).fold(
                                onFailure = { err -> Result.failure(err) },
                                onSuccess = {
                                    storage.updateUserId(prefs.id, session.user.id).fold(
                                        onFailure = { err ->
                                            if (err is ConstraintViolationException) {
                                                Result.success(prefs)
                                            } else {
                                                Result.failure(err)
                                            }
                                        },
                                        onSuccess = { Result.success(prefs) }
                                    )
                                }
                            )
                        }
                    }
                )
            }.fold(
                onFailure = { err -> emit(Result.failure(err)) },
                onSuccess = { prefs -> emit(Result.success(prefs)) }
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createCloudPreferences(prefs: UserPreferences): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { s ->
            client.create(
                uid = s.user.id.toString(),
                dto = CreateUserPreferencesDto(
                    theme = prefs.theme.name,
                    onboardingCompleted = prefs.onboardingCompleted,
                    language = prefs.language
                ),
                token = s.accessToken
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateCloudPreferences(prefs: UserPreferences): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { s ->
            client.update(
                pid = prefs.id.toString(),
                dto = UpdateUserPreferencesDto(
                    theme = prefs.theme.name,
                    language = prefs.language
                ),
                token = s.accessToken
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePreferences(prefs: UserPreferences): Result<Unit> {
        return storage.upsert(prefs.copy(updatedAt = Option.of(Clock.System.now())))
    }
}
