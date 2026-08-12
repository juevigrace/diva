package com.diva.models.api.user.profile.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileResponse(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("birth_date")
    val birthDate: Long,
    @SerialName("phone_number")
    val phoneNumber: String = "",
    @SerialName("alias")
    val alias: String,
    @SerialName("avatar")
    val avatar: String = "",
    @SerialName("bio")
    val bio: String,
)
