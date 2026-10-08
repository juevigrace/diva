package com.diva.app.feed.presentation.state

import com.diva.app.media.models.Media

data class FeedState(
    val forYouMedia: List<Media> = emptyList(),
    val recentMedia: List<Media> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
