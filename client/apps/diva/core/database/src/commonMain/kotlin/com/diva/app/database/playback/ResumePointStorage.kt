package com.diva.app.database.playback

import com.diva.app.models.media.Media
import com.diva.app.models.playback.ResumePoint
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface ResumePointStorage {
    @OptIn(ExperimentalUuidApi::class)
    suspend fun getByUserAndMedia(userId: Uuid, mediaId: Uuid): Result<Option<ResumePoint>>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun upsert(item: ResumePoint): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun delete(userId: Uuid, mediaId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun deleteCompletedByUser(userId: Uuid): Result<Unit>

    @OptIn(ExperimentalUuidApi::class)
    suspend fun getResumableByUser(userId: Uuid): Result<List<Media>>

    @OptIn(ExperimentalUuidApi::class)
    fun getResumableByUserFlow(userId: Uuid): Flow<Result<List<Media>>>
}
