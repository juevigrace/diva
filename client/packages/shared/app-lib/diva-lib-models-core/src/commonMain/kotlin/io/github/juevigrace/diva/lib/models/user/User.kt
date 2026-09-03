package io.github.juevigrace.diva.lib.models.user

import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.api.user.UserResponse
import io.github.juevigrace.diva.lib.models.user.actions.UserAction
import io.github.juevigrace.diva.lib.models.user.device.UserDevice
import io.github.juevigrace.diva.lib.models.user.permissions.UserPermission
import io.github.juevigrace.diva.lib.models.user.preferences.UserPreferences
import io.github.juevigrace.diva.lib.models.user.profile.UserProfile
import io.github.juevigrace.diva.lib.models.user.state.UserState
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalJsExport::class)
@JsExport
data class User(
    val id: Uuid,
    val email: String = "",
    val username: String = "",
    val phoneNumber: String = "",
    val passwordHash: Option<String> = Option.None,
    val role: Role = Role.USER,
    val state: Option<UserState> = Option.None,
    val profile: Option<UserProfile> = Option.None,
    val devices: List<UserDevice> = emptyList(),
    val actions: List<UserAction> = emptyList(),
    val permissions: List<UserPermission> = emptyList(),
    val preferences: Option<UserPreferences> = Option.None,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val deletedAt: Option<Instant> = Option.None,
) {
    companion object {
        fun fromResponse(response: UserResponse): User {
            return User(
                id = Uuid.parse(response.id),
                email = response.email,
                username = response.username,
                phoneNumber = response.phoneNumber,
                role = safeRole(response.role),
                state = Option.of(response.state?.let { UserState.fromResponse(it) }),
                createdAt = Instant.fromEpochMilliseconds(response.createdAt),
                updatedAt = Instant.fromEpochMilliseconds(response.updatedAt),
                deletedAt = Option.of(response.deletedAt?.let { Instant.fromEpochMilliseconds(it) }),
            )
        }
    }
}
