@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.state.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateVerifiedDto(
    @SerialName("verified")
    val verified: Boolean,
)
