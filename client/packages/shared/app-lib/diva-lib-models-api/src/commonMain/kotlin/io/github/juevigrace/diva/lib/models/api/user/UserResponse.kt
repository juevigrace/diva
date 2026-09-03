package io.github.juevigrace.diva.lib.models.api.user

import io.github.juevigrace.diva.lib.models.api.user.state.UserStateResponse
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UserResponse(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("email")
    val email: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("role")
    val role: String,
    @SerialName("state")
    val state: UserStateResponse? = null,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
    @SerialName("deleted_at")
    val deletedAt: Long? = null,
)
