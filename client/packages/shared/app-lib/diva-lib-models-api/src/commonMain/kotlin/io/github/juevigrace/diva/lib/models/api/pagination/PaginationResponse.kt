package io.github.juevigrace.diva.lib.models.api.pagination

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class PaginatedResponse<T>(
    @SerialName("items")
    val items: List<T>,
    @SerialName("pagination_info")
    val pagination: PaginationInfo,
)
