@file:DivaJsExport

package io.github.juevigrace.diva.lib.verification.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyActionDto(
    @SerialName("action_id")
    val actionId: String,
    @SerialName("token")
    val token: String,
)
