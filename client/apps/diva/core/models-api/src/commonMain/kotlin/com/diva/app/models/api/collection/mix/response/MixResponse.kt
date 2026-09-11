@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.api.collection.mix.response

import com.diva.app.models.api.collection.response.CollectionResponse
import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MixResponse(
    @SerialName("collection_id")
    val collectionId: String,
    @SerialName("algorithm_type")
    val algorithmType: String = "trending",
    @SerialName("time_window_hours")
    val timeWindowHours: Int = 24,
    @SerialName("content_weight")
    val contentWeight: Float = 0.7f,
    @SerialName("freshness_weight")
    val freshnessWeight: Float = 0.3f,
    @SerialName("min_engagement_score")
    val minEngagementScore: Int = 10,
    @SerialName("excluded_tags")
    val excludedTags: String = "",
    @SerialName("auto_refresh")
    val autoRefresh: Boolean = true,
    @SerialName("refresh_interval_seconds")
    val refreshIntervalSeconds: Int = 3600,
)
