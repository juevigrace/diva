@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package com.diva.app.models.collection.mix

import com.diva.app.models.api.collection.mix.response.MixResponse
import com.diva.app.models.collection.Collection
import io.github.juevigrace.diva.core.DivaJsExport
import kotlin.js.ExperimentalJsExport
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Mix(
    val collection: Collection,
    val algorithmType: String = "trending",
    val timeWindowHours: Int = 24,
    val contentWeight: Float = 0.7f,
    val freshnessWeight: Float = 0.3f,
    val minEngagementScore: Int = 10,
    val excludedTags: String = "",
    val autoRefresh: Boolean = true,
    val refreshIntervalSeconds: Int = 3600,
) {
    companion object {
        fun fromResponse(response: MixResponse): Mix {
            return Mix(
                collection = Collection(
                    id = Uuid.parse(response.collectionId),
                    name = "",
                ),
                algorithmType = response.algorithmType,
                timeWindowHours = response.timeWindowHours,
                contentWeight = response.contentWeight,
                freshnessWeight = response.freshnessWeight,
                minEngagementScore = response.minEngagementScore,
                excludedTags = response.excludedTags,
                autoRefresh = response.autoRefresh,
                refreshIntervalSeconds = response.refreshIntervalSeconds,
            )
        }
    }
}
