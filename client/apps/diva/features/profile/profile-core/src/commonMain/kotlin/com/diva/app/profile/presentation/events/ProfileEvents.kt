package com.diva.app.profile.presentation.events

sealed interface ProfileEvents {
    data object OnBack : ProfileEvents
}