package io.github.juevigrace.diva.lib.models.user.preferences

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.lib.models.Theme
import io.github.juevigrace.diva.lib.models.api.user.preferences.UserPreferencesResponse
import io.github.juevigrace.diva.lib.models.safeValueOfTheme
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalJsExport::class)
@JsExport
data class UserPreferences(
    val id: Uuid = Uuid.NIL,
    val userId: Uuid = Uuid.NIL,
    val theme: Theme = Theme.SYSTEM,
    val onboardingCompleted: Boolean = false,
    val language: String = "en",
    val lastSyncAt: Option<Instant> = Option.None,
    val createdAt: Option<Instant> = Option.None,
    val updatedAt: Option<Instant> = Option.None,
) {
    companion object {
        fun fromResponse(response: UserPreferencesResponse): UserPreferences {
            return UserPreferences(
                id = Uuid.parse(response.id),
                theme = safeValueOfTheme(response.theme),
                onboardingCompleted = response.onboardingCompleted,
                language = response.language,
                lastSyncAt = Option.of(Instant.fromEpochMilliseconds(response.lastSyncAt)),
                createdAt = Option.of(Instant.fromEpochMilliseconds(response.createdAt)),
                updatedAt = Option.of(Instant.fromEpochMilliseconds(response.updatedAt)),
            )
        }
    }
}
