package com.diva.models.api.user.profile.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileDto(
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("alias")
    val alias: String,
    @SerialName("bio")
    val bio: String = "",
    @SerialName("birth_date")
    val birthDate: Long,
)
