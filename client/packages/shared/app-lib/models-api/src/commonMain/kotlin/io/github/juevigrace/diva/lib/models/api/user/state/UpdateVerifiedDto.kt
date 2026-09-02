package io.github.juevigrace.diva.lib.models.api.user.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateVerifiedDto(
    @SerialName("verified")
    val verified: Boolean,
)
