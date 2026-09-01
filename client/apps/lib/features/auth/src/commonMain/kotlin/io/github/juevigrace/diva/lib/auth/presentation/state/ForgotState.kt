package io.github.juevigrace.diva.lib.auth.presentation.state

import io.github.juevigrace.diva.lib.models.auth.EmailForm

data class ForgotState(
    val form: EmailForm = EmailForm(),
)
