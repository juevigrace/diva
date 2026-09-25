package com.diva.app.player.presentation.events

sealed interface PlayerEvents {
    data object OnBack : PlayerEvents
}