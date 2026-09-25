package com.diva.app.features.profile.presentation.events

sealed interface ProfileEvents {
    data object OnBack : ProfileEvents
}