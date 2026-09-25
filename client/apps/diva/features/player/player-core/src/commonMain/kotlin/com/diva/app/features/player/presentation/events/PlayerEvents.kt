package com.diva.app.features.player.presentation.events

sealed interface PlayerEvents {
    data object OnBack : PlayerEvents
}