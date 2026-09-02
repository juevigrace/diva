package io.github.juevigrace.diva.lib.models.api.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePhoneNumberDto(
    @SerialName("phone_number")
    val phoneNumber: String,
)
