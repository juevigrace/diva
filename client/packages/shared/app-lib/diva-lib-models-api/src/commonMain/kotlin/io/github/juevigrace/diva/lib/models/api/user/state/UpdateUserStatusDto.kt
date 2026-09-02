package io.github.juevigrace.diva.lib.models.api.user.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserStatusDto(
    @SerialName("status")
    val status: String,
)
