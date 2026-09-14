package com.diva.app.features.mix.database

import com.diva.app.database.DivaDB
import com.diva.app.database.collection.mix.MixMetadataStorage
import com.diva.app.models.collection.Collection
import com.diva.app.models.collection.mix.Mix
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.database.DivaDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import migrations.Diva_mix_metadata

@OptIn(ExperimentalUuidApi::class)
class MixMetadataStorageImpl(
    private val db: DivaDatabase<DivaDB>,
) : MixMetadataStorage {

    override suspend fun getByCollection(collectionId: Uuid): Result<Option<Mix>> {
        return db.getOne { mixMetadataQueries.findByCollection(collectionId.toString(), ::mapToMix) }
    }

    override fun getByCollectionFlow(collectionId: Uuid): Flow<Result<Option<Mix>>> {
        return db.getOneAsFlow { mixMetadataQueries.findByCollection(collectionId.toString(), ::mapToMix) }
    }

    override suspend fun upsert(collectionId: Uuid, item: Mix): Result<Unit> {
        return db.use {
            transaction {
                mixMetadataQueries.upsert(
                    Diva_mix_metadata(
                        collection_id = collectionId.toString(),
                        algorithm_type = item.algorithmType,
                        time_window_hours = item.timeWindowHours.toLong(),
                        content_weight = item.contentWeight.toDouble(),
                        freshness_weight = item.freshnessWeight.toDouble(),
                        min_engagement_score = item.minEngagementScore.toLong(),
                        excluded_tags = item.excludedTags,
                        auto_refresh = item.autoRefresh,
                        refresh_interval_seconds = item.refreshIntervalSeconds.toLong(),
                    )
                )
            }
        }
    }

    override suspend fun deleteByCollection(collectionId: Uuid): Result<Unit> {
        return db.use {
            transaction {
                mixMetadataQueries.deleteByCollection(collectionId.toString())
            }
        }
    }

    private fun mapToMix(
        collectionId: String,
        algorithmType: String,
        timeWindowHours: Long,
        contentWeight: Double,
        freshnessWeight: Double,
        minEngagementScore: Long,
        excludedTags: String,
        autoRefresh: Boolean,
        refreshIntervalSeconds: Long,
    ): Mix = Mix(
        collection = Collection(id = collectionId),
        algorithmType = algorithmType,
        timeWindowHours = timeWindowHours.toInt(),
        contentWeight = contentWeight.toFloat(),
        freshnessWeight = freshnessWeight.toFloat(),
        minEngagementScore = minEngagementScore.toInt(),
        excludedTags = excludedTags,
        autoRefresh = autoRefresh,
        refreshIntervalSeconds = refreshIntervalSeconds.toInt(),
    )
}