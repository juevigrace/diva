package com.diva.app.feed.presentation.events

sealed interface FeedEvents {
    data object Refresh : FeedEvents
    data class OnSelectMedia(val mediaId: String) : FeedEvents
}
