package com.diva.app.models.collection.mix

import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
data class MixMetadata(
    val algorithmType: String = "trending",
    val timeWindowHours: Int = 24,
    val contentWeight: Float = 0.7f,
    val freshnessWeight: Float = 0.3f,
    val minEngagementScore: Int = 10,
    val excludedTags: String = "",
    val autoRefresh: Boolean = true,
    val refreshIntervalSeconds: Int = 3600,
)
