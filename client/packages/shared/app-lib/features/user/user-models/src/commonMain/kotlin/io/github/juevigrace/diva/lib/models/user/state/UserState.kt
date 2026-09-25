@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.state

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.user.api.state.UserStateResponse
import io.github.juevigrace.diva.lib.models.user.UserStatus
import io.github.juevigrace.diva.lib.models.user.safeUserStatus
import kotlin.js.ExperimentalJsExport

data class UserState(
    val verified: Boolean = false,
    val status: UserStatus = UserStatus.ACTIVE,
    val lastActiveAt: Option<Long> = None,
    val updatedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: UserStateResponse): UserState {
            return UserState(
                verified = response.verified,
                status = safeUserStatus(response.status),
                lastActiveAt = Option.of(response.lastActiveAt),
                updatedAt = Option.of(response.updatedAt),
            )
        }
    }
}
