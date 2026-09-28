@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.media.tag.models.api

import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagResponse(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("updated_at")
    val updatedAt: Long,
    @SerialName("deleted_at")
    val deletedAt: Long? = null,
)
