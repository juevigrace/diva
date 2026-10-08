package com.diva.app.feed.domain

import com.diva.app.media.models.Media
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun getForYouMedia(): Flow<Result<List<Media>>>
    fun getRecentMedia(): Flow<Result<List<Media>>>
}
