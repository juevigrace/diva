package com.diva.models.api.user.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePhoneNumberDto(
    @SerialName("phone_number")
    val phoneNumber: String,
)
