package com.diva.app.database.playback

import com.diva.app.models.media.Media
import com.diva.app.models.playback.ResumePoint
import io.github.juevigrace.diva.core.Option
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface ResumePointStorage {
    suspend fun getByUserAndMedia(userId: Uuid, mediaId: Uuid): Result<Option<ResumePoint>>

    suspend fun upsert(item: ResumePoint): Result<Unit>

    suspend fun delete(userId: Uuid, mediaId: Uuid): Result<Unit>

    suspend fun deleteCompletedByUser(userId: Uuid): Result<Unit>

    suspend fun getResumableByUser(userId: Uuid): Result<List<Media>>

    fun getResumableByUserFlow(userId: Uuid): Flow<Result<List<Media>>>
}
