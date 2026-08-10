package com.diva.models.user.profile

import com.diva.models.api.user.profile.response.UserProfileResponse
import io.github.juevigrace.diva.core.Option
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserProfile(
    val userId: Uuid = Uuid.NIL,
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: Option<Instant> = Option.None,
    val alias: String = "",
    val avatar: String = "",
    val bio: String = "",
    val updatedAt: Option<Instant> = Option.None,
) {
    companion object {
        fun fromResponse(response: UserProfileResponse): UserProfile {
            return UserProfile(
                firstName = response.firstName,
                lastName = response.lastName,
                birthDate = Option.of(Instant.fromEpochMilliseconds(response.birthDate)),
                alias = response.alias,
                avatar = response.avatar,
                bio = response.bio,
            )
        }
    }
}
