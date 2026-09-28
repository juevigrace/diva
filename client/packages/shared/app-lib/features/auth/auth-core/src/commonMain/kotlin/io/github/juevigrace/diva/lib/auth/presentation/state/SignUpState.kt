package io.github.juevigrace.diva.lib.auth.presentation.state

import io.github.juevigrace.diva.lib.auth.models.SignUpForm

data class SignUpState(
    val form: SignUpForm = SignUpForm(),
)
