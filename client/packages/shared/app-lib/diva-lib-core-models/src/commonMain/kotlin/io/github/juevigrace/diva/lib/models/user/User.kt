@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user

import io.github.juevigrace.diva.core.DivaJsExport
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
