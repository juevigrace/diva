@file:DivaJsExport

package com.diva.app.collection.mix.models

import com.diva.app.collection.mix.models.api.MixResponse
import com.diva.app.collection.models.Collection
import io.github.juevigrace.diva.core.DivaJsExport

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
                    id = response.collectionId,
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
