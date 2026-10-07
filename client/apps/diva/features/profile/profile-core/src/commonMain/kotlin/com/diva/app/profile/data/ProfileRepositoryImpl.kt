package com.diva.app.profile.data

import com.diva.app.profile.domain.ProfileRepository
import com.diva.app.profile.models.Profile
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.session.domain.SessionRepository
import io.github.juevigrace.diva.lib.session.domain.observeSession
import io.github.juevigrace.diva.lib.user.domain.UserDevicesRepository
import io.github.juevigrace.diva.lib.user.domain.UserPreferencesRepository
import io.github.juevigrace.diva.lib.user.domain.UserProfileRepository
import io.github.juevigrace.diva.lib.user.domain.UserRepository
import io.github.juevigrace.diva.lib.user.domain.UserStateRepository
import io.github.juevigrace.diva.lib.user.models.UserStatus
import io.github.juevigrace.diva.lib.user.preferences.models.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ProfileRepositoryImpl(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
    private val userStateRepository: UserStateRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val userDevicesRepository: UserDevicesRepository,
) : ProfileRepository {
    override fun observe(): Flow<Result<Profile>> {
        return observeSession(sessionRepository::observe) { session ->
            combine(
                userRepository.observe(session.userId),
                userProfileRepository.observe(),
                userStateRepository.observe(),
                userPreferencesRepository.observe(),
                userDevicesRepository.observe(),
            ) { userResult, profileResult, stateResult, preferencesResult, devicesResult ->
                val user = userResult.getOrNull()
                val profile = profileResult.getOrNull()
                val state = stateResult.getOrNull()
                val preferences = preferencesResult.getOrNull()

                Result.success(
                    Profile(
                        username = user?.username ?: "",
                        email = user?.email ?: None,
                        phoneNumber = profile?.phoneNumber?.takeIf { it.isNotBlank() }
                            ?.let { Option.of(it) }
                            ?: user?.phoneNumber
                            ?: None,
                        role = user?.role ?: Role.USER,
                        alias = profile?.alias ?: "",
                        bio = profile?.bio ?: "",
                        avatar = profile?.avatar ?: "",
                        verified = state?.verified ?: false,
                        status = state?.status ?: UserStatus.ACTIVE,
                        theme = preferences?.theme ?: Theme.SYSTEM,
                        language = preferences?.language ?: "en",
                        devices = devicesResult.getOrDefault(defaultValue = emptyList()).map { it.device.name },
                    )
                )
            }
        }
    }
}
