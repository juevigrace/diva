package io.github.juevigrace.diva.lib.models.api.pagination

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class PaginationInfo(
    @SerialName("page")
    val page: Int,
    @SerialName("limit")
    val limit: Int,
    @SerialName("total_items")
    val totalItems: Long,
    @SerialName("total_pages")
    val totalPages: Int,
)

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class PaginatedResponse<T>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("pagination_info")
    val pagination: PaginationInfo,
)
