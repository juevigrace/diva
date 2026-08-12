package com.diva.models.api.user.state.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateVerifiedDto(
    @SerialName("verified")
    val verified: Boolean,
)
