package io.github.juevigrace.diva.lib.models.api.user.action

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class UserActionResponse(
    @SerialName("id")
    val id: String,
    @SerialName("action_name")
    val actionName: String,
)
