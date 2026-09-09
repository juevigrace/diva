package com.diva.app.features.mix.presentation.events

sealed interface MixEvents {
    data object OnBack : MixEvents
}