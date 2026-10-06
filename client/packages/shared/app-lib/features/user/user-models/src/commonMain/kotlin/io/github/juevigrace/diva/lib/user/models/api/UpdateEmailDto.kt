@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmailDto(
    @SerialName("email")
    val email: String,
)
