package com.diva.app.features.profile.data

import com.diva.app.features.profile.domain.Profile
import com.diva.app.features.profile.domain.ProfileRepository
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.lib.models.Theme
import io.github.juevigrace.diva.lib.models.user.Role
import io.github.juevigrace.diva.lib.models.user.UserStatus
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import io.github.juevigrace.diva.lib.models.user.state.UserState
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userStateRepository: UserStateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userDevicesRepository: UserDevicesRepository,
) : ProfileRepository {

    override fun observeProfile(): Flow<Result<Profile?>> {
        return sessionRepository.getCurrentSession().flatMapLatest { sessionResult ->
            val userId = sessionResult.orNull()?.getOrNull()?.user?.id
                ?: return@flatMapLatest flowOf(Result.success<Profile?>(null))

            combine(
                userRepository.getUser(userId),
                userProfileRepository.getProfile(userId),
                userStateRepository.getState(userId),
                userPreferencesRepository.getPreferences(userId),
                userDevicesRepository.getDevices(userId),
            ) { userResult, profileResult, stateResult, preferencesResult, devicesResult ->
                val user = userResult.orValue()
                val profile = profileResult.orValue()
                val state = stateResult.orValue()
                val preferences = preferencesResult.orValue()

                Result.success<Profile?>(
                    Profile(
                        username = user?.username ?: "",
                        email = user?.email?.getOrNull(),
                        phoneNumber = profile?.phoneNumber?.takeIf { it.isNotBlank() }
                            ?: user?.phoneNumber?.getOrNull(),
                        role = user?.role ?: Role.USER,
                        alias = profile?.alias ?: "",
                        bio = profile?.bio ?: "",
                        avatar = profile?.avatar ?: "",
                        verified = state?.verified ?: false,
                        status = state?.status ?: UserStatus.ACTIVE,
                        theme = preferences?.theme ?: Theme.SYSTEM,
                        language = preferences?.language ?: "en",
                        devices = devicesResult.orEmpty().map { it.device.name },
                    )
                )
            }
        }
    }
}

private fun <T> Result<T>.orNull(): T? = fold(onSuccess = { it }, onFailure = { null })

private fun <T : Any> Result<Option<T>>.orValue(): T? = orNull()?.getOrNull()

private fun <T> Result<List<T>>.orEmpty(): List<T> = getOrDefault(emptyList())