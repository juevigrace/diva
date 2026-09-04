@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.auth

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.api.auth.signin.SignInDto
import io.github.juevigrace.diva.lib.models.session.SessionData
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
            sessionData = sessionData.toSessionDataDto(),
        )
    }
}
