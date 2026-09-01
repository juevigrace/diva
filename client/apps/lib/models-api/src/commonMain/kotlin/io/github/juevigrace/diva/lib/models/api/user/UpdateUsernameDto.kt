package io.github.juevigrace.diva.lib.models.api.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUsernameDto(
    @SerialName("username")
    val username: String,
)
