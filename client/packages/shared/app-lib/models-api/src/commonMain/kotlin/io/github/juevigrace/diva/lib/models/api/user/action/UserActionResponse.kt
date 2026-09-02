package io.github.juevigrace.diva.lib.models.api.user.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserActionResponse(
    @SerialName("id")
    val id: String,
    @SerialName("action_name")
    val actionName: String,
)
