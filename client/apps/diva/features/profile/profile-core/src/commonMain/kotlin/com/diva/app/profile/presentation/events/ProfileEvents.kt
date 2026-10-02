package com.diva.app.profile.presentation.events

sealed interface ProfileEvents {
    data object OnOpenSettings : ProfileEvents

    data object OnOpenAccount : ProfileEvents

    data object OnOpenDevices : ProfileEvents

    data object OnOpenPermissions : ProfileEvents

    data object OnSignOut : ProfileEvents
}