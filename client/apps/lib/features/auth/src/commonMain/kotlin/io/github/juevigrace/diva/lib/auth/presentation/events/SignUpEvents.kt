package io.github.juevigrace.diva.lib.auth.presentation.events

sealed interface SignUpEvents {
    data object OnBack : SignUpEvents
}
