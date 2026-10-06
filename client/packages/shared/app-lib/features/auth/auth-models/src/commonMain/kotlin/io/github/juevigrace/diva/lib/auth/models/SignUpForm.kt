@file:DivaJsExport

package io.github.juevigrace.diva.lib.auth.models

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.auth.models.api.SignUpDto
import io.github.juevigrace.diva.lib.session.models.SessionData
import io.github.juevigrace.diva.lib.user.models.api.CreateUserDto

data class SignUpForm(
    val email: String = "",
    val isEmailTaken: Boolean = false,
    val username: String = "",
    val isUsernameTaken: Boolean = false,
    val alias: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsAndConditions: Boolean = false,
    val privacyPolicy: Boolean = false,
    val sessionData: SessionData = SessionData(),
) {
    fun toSignUpDto(): SignUpDto {
        return SignUpDto(
            user = CreateUserDto(
                email = email,
                username = username,
                password = password,
            ),
            sessionData = sessionData.toDto(),
        )
    }
}
