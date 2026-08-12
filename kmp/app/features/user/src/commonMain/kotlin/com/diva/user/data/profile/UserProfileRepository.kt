package com.diva.user.data.profile

import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.api.user.profile.dtos.CreateProfileDto
import com.diva.models.api.user.profile.dtos.UpdateProfileDto
import com.diva.models.user.profile.UserProfile
import com.diva.user.api.client.profile.UserProfileApi
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.getOrElse
import kotlin.fold
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

interface UserProfileRepository : Repository {
    suspend fun getProfile(): Result<UserProfile?>
    suspend fun createProfile(profile: UserProfile): Result<Unit>
    suspend fun updateProfile(profile: UserProfile): Result<Unit>
}

class UserProfileRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val client: UserProfileApi,
) : UserProfileRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getProfile(): Result<UserProfile?> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.getByUser(session.user.id.toString(), session.accessToken).fold(
                onFailure = { err -> Result.failure(err) },
                onSuccess = { res -> Result.success(res?.let { UserProfile.fromResponse(it) }) }
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createProfile(profile: UserProfile): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.create(
                uid = session.user.id.toString(),
                dto = CreateProfileDto(
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    alias = profile.alias,
                    bio = profile.bio,
                    birthDate = profile.birthDate.getOrElse { Instant.fromEpochMilliseconds(0) }.toEpochMilliseconds(),
                ),
                token = session.accessToken
            )
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updateProfile(profile: UserProfile): Result<Unit> {
        return withSession(sessionRepository::getCurrent) { session ->
            client.update(
                uid = session.user.id.toString(),
                dto = UpdateProfileDto(
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    alias = profile.alias,
                    bio = profile.bio,
                    birthDate = profile.birthDate.getOrElse { Instant.fromEpochMilliseconds(0) }.toEpochMilliseconds(),
                ),
                token = session.accessToken
            )
        }
    }
}
