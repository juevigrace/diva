package com.diva.models.user.state

import com.diva.models.api.user.state.response.UserStateResponse
import com.diva.models.user.UserStatus
import com.diva.models.user.safeUserStatus
import io.github.juevigrace.diva.core.Option
import kotlin.time.Instant

data class UserState(
    val verified: Boolean = false,
    val status: UserStatus = UserStatus.ACTIVE,
    val lastActiveAt: Option<Instant> = Option.None,
    val updatedAt: Option<Instant> = Option.None,
) {
    companion object {
        fun fromResponse(response: UserStateResponse): UserState {
            return UserState(
                verified = response.verified,
                status = safeUserStatus(response.status),
                lastActiveAt = Option.of(Instant.fromEpochMilliseconds(response.lastActiveAt)),
                updatedAt = Option.of(Instant.fromEpochMilliseconds(response.updatedAt)),
            )
        }
    }
}
