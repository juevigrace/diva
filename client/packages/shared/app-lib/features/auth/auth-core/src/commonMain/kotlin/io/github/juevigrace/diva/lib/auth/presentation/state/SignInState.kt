package io.github.juevigrace.diva.lib.auth.presentation.state

import io.github.juevigrace.diva.lib.models.auth.SignInForm

data class SignInState(
    val form: SignInForm = SignInForm(),
)
