@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.core.models.Role
import io.github.juevigrace.diva.lib.core.models.safeRole
import io.github.juevigrace.diva.lib.user.actions.models.UserAction
import io.github.juevigrace.diva.lib.user.device.models.UserDevice
import io.github.juevigrace.diva.lib.user.models.api.UserResponse
import io.github.juevigrace.diva.lib.user.permissions.models.UserPermission
import io.github.juevigrace.diva.lib.user.preferences.models.UserPreferences
import io.github.juevigrace.diva.lib.user.profile.models.UserProfile
import io.github.juevigrace.diva.lib.user.state.models.UserState
import kotlin.time.Clock

data class User(
    val id: String,
    val email: Option<String> = None,
    val username: String = "",
    val phoneNumber: Option<String> = None,
    val passwordHash: Option<String> = None,
    val role: Role = Role.USER,
    val state: Option<UserState> = None,
    val profile: Option<UserProfile> = None,
    val devices: List<UserDevice> = emptyList(),
    val actions: List<UserAction> = emptyList(),
    val permissions: List<UserPermission> = emptyList(),
    val preferences: Option<UserPreferences> = None,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
    val deletedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: UserResponse): User {
            return User(
                id = response.id,
                email = Option.of(response.email),
                username = response.username,
                phoneNumber = Option.of(response.phoneNumber),
                role = safeRole(response.role),
                state = Option.of(response.state?.let { UserState.fromResponse(it) }),
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
                deletedAt = Option.of(response.deletedAt),
            )
        }
    }
}
