@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.profile

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.user.profile.UserProfileResponse
import kotlin.js.ExperimentalJsExport
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserProfile(
    val userId: Uuid = Uuid.NIL,
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: Option<Instant> = None,
    val phoneNumber: String = "",
    val alias: String = "",
    val avatar: String = "",
    val bio: String = "",
    val updatedAt: Option<Instant> = None,
) {
    companion object {
        fun fromResponse(response: UserProfileResponse): UserProfile {
            return UserProfile(
                firstName = response.firstName,
                lastName = response.lastName,
                birthDate = Option.of(Instant.fromEpochMilliseconds(response.birthDate)),
                phoneNumber = response.phoneNumber,
                alias = response.alias,
                avatar = response.avatar,
                bio = response.bio,
            )
        }
    }
}
