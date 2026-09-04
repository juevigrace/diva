package com.diva.app.features.home.presentation.events

sealed interface HomeEvents {
    data object OnBack : HomeEvents
}
