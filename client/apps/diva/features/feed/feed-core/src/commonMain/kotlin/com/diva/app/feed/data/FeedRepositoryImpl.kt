package com.diva.app.feed.data

import com.diva.app.feed.domain.FeedRepository
import com.diva.app.media.models.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FeedRepositoryImpl : FeedRepository {
    override fun getForYouMedia(): Flow<Result<List<Media>>> = flowOf(Result.success(emptyList()))

    override fun getRecentMedia(): Flow<Result<List<Media>>> = flowOf(Result.success(emptyList()))
}
