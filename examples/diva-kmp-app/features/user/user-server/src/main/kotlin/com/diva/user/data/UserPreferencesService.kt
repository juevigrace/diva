package com.diva.user.data

import com.diva.database.user.UserStorage
import com.diva.models.api.ApiResponse
import com.diva.models.api.user.preferences.dtos.UserPreferencesDto
import com.diva.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.map
import io.github.juevigrace.diva.core.tryResult
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface UserPreferencesService {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun createPreferences(userId: Uuid, dto: UserPreferencesDto): DivaResult<ApiResponse<Unit>, DivaError>

    suspend fun updatePreferences(dto: UserPreferencesDto): DivaResult<ApiResponse<Unit>, DivaError>
}

class UserPreferencesServiceImpl(
    private val storage: UserStorage,
) : UserPreferencesService {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createPreferences(
        userId: Uuid,
        dto: UserPreferencesDto
    ): DivaResult<ApiResponse<Unit>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            storage.insertCloudPreferences(
                userId = userId,
                prefs = UserPreferences.fromDto(dto)
            ).map { ApiResponse(message = "Success") }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun updatePreferences(dto: UserPreferencesDto): DivaResult<ApiResponse<Unit>, DivaError> {
        return tryResult(
            onError = { e -> e.toDivaError() }
        ) {
            storage.updatePreferences(UserPreferences.fromDto(dto)).map { ApiResponse(message = "Success") }
        }
    }
}
