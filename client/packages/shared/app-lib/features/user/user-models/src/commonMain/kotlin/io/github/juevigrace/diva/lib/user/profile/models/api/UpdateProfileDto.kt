@file:DivaJsExport

package io.github.juevigrace.diva.lib.user.profile.models.api

import io.github.juevigrace.diva.core.DivaJsExport
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
