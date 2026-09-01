package io.github.juevigrace.diva.lib.auth.presentation.state

import io.github.juevigrace.diva.lib.models.auth.SignUpForm

data class SignUpState(
    val form: SignUpForm = SignUpForm(),
)
