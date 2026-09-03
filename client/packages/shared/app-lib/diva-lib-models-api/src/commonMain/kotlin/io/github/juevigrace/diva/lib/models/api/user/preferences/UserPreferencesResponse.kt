package io.github.juevigrace.diva.lib.models.api.user.preferences

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UserPreferencesResponse(
    @SerialName("id")
    val id: String,
    @SerialName("theme")
    val theme: String,
    @SerialName("onboarding_completed")
    val onboardingCompleted: Boolean,
    @SerialName("language")
    val language: String,
    @SerialName("last_sync_at")
    val lastSyncAt: Long,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
)
