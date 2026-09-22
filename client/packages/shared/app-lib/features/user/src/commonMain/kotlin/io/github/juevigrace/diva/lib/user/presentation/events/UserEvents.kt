package io.github.juevigrace.diva.lib.user.presentation.events

sealed interface UserEvents {
    data object OnBack : UserEvents
}
