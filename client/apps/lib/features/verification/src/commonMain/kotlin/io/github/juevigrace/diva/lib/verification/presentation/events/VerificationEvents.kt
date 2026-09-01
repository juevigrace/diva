package io.github.juevigrace.diva.lib.verification.presentation.events

sealed interface VerificationEvents {
    data object OnBack : VerificationEvents
}
