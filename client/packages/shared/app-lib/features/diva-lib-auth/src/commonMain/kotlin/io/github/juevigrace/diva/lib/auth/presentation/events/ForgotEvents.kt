package io.github.juevigrace.diva.lib.auth.presentation.events

sealed interface ForgotEvents {
    data object OnBack : ForgotEvents
}
