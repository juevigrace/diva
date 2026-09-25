package com.diva.app.features.media.presentation.events

sealed interface MediaEvents {
    data object OnBack : MediaEvents
}