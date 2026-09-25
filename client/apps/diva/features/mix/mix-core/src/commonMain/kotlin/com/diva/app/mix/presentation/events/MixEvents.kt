package com.diva.app.mix.presentation.events

sealed interface MixEvents {
    data object OnBack : MixEvents
}