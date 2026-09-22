@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.profile

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.user.profile.UserProfileResponse
import kotlin.js.ExperimentalJsExport

data class UserProfile(
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: Option<Long> = None,
    val phoneNumber: String = "",
    val alias: String = "",
    val avatar: String = "",
    val bio: String = "",
    val updatedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: UserProfileResponse): UserProfile {
            return UserProfile(
                firstName = response.firstName,
                lastName = response.lastName,
                birthDate = Option.of(response.birthDate),
                phoneNumber = response.phoneNumber,
                alias = response.alias,
                avatar = response.avatar,
                bio = response.bio,
            )
        }
    }
}
