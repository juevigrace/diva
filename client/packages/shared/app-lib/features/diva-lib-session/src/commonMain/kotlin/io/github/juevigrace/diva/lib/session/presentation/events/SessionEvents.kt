package io.github.juevigrace.diva.lib.session.presentation.events

sealed interface SessionEvents {
    data object OnBack : SessionEvents
}
