package io.github.juevigrace.diva.lib.auth.presentation.state

import io.github.juevigrace.diva.lib.auth.models.SignInForm

data class SignInState(
    val form: SignInForm = SignInForm(),
)
