package com.diva.app.server.presentation.events

sealed interface ServerEvents {
    data object OnBack : ServerEvents
}