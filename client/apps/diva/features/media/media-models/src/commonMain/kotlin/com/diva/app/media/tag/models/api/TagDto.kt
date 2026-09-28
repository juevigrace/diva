@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.media.tag.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    @SerialName("name")
    val name: String,
)
