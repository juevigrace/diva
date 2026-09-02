package io.github.juevigrace.diva.lib.auth.presentation.events

sealed interface SignInEvents {
    data object OnBack : SignInEvents
}
