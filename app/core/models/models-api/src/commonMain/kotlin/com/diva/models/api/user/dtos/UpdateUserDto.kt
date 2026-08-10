package com.diva.models.api.user.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    @SerialName("first_name")
    val firstName: String = "",
    @SerialName("last_name")
    val lastName: String = "",
    @SerialName("alias")
    val alias: String = "",
    @SerialName("birth_date")
    val birthDate: Long = 0,
    @SerialName("bio")
    val bio: String = "",
    @SerialName("avatar")
    val avatar: String = ""
)
