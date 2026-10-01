package com.diva.app.player.presentation.events

import com.diva.app.media.models.Media

sealed interface PlayerEvents {
    data object OnBack : PlayerEvents

    data object OnExpand : PlayerEvents

    data object OnTogglePlayPause : PlayerEvents

    data object OnNext : PlayerEvents

    data object OnPrevious : PlayerEvents

    data object OnToggleShuffle : PlayerEvents

    data object OnCycleRepeatMode : PlayerEvents

    data object OnToggleLyrics : PlayerEvents

    data class OnScrub(val fraction: Float) : PlayerEvents

    data class OnOpenQueueItem(val media: Media) : PlayerEvents
}