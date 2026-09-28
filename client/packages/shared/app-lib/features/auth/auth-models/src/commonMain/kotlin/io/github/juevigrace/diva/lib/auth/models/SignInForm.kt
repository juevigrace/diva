@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.auth.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.auth.models.api.SignInDto
import io.github.juevigrace.diva.lib.session.models.SessionData
import kotlin.js.ExperimentalJsExport

data class SignInForm(
    val username: String = "",
    val password: String = "",
    val sessionData: SessionData = SessionData(),
) {
    fun toSignInDto(): SignInDto {
        return SignInDto(
            username = username,
            password = password,
            sessionData = sessionData.toDto(),
        )
    }
}
