@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.user.preferences

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.core.None
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.Theme
import io.github.juevigrace.diva.lib.models.api.user.preferences.UserPreferencesResponse
import io.github.juevigrace.diva.lib.models.safeValueOfTheme
import kotlin.js.ExperimentalJsExport

data class UserPreferences(
    val id: String = "",
    val theme: Theme = Theme.SYSTEM,
    val onboardingCompleted: Boolean = false,
    val language: String = "en",
    val lastSyncAt: Option<Long> = None,
    val createdAt: Option<Long> = None,
    val updatedAt: Option<Long> = None,
) {
    companion object {
        fun fromResponse(response: UserPreferencesResponse): UserPreferences {
            return UserPreferences(
                id = response.id,
                theme = safeValueOfTheme(response.theme),
                onboardingCompleted = response.onboardingCompleted,
                language = response.language,
                lastSyncAt = Option.of(response.lastSyncAt),
                createdAt = Option.of(response.createdAt),
                updatedAt = Option.of(response.updatedAt),
            )
        }
    }
}
