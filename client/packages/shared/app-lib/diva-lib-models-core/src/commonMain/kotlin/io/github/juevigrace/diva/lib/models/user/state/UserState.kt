package io.github.juevigrace.diva.lib.models.user.state

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.user.state.UserStateResponse
import io.github.juevigrace.diva.lib.models.user.UserStatus
import io.github.juevigrace.diva.lib.models.user.safeUserStatus
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class UserState(
    val userId: Uuid = Uuid.NIL,
    val verified: Boolean = false,
    val status: UserStatus = UserStatus.ACTIVE,
    val lastActiveAt: Option<Instant> = Option.None,
    val updatedAt: Option<Instant> = Option.None,
) {
    companion object {
        fun fromResponse(response: UserStateResponse): UserState {
            return UserState(
                userId = Uuid.NIL,
                verified = response.verified,
                status = safeUserStatus(response.status),
                lastActiveAt = Option.of(response.lastActiveAt.let { Instant.fromEpochMilliseconds(it) }),
                updatedAt = Option.of(response.updatedAt.let { Instant.fromEpochMilliseconds(it) }),
            )
        }
    }
}
