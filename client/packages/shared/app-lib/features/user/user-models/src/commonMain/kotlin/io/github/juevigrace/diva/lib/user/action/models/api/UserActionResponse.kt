@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.action.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserActionResponse(
    @SerialName("id")
    val id: String,
    @SerialName("action_name")
    val actionName: String,
)
