package com.diva.app.media.presentation.events

sealed interface MediaEvents {
    data object OnBack : MediaEvents
}