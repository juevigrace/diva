@file:OptIn(ExperimentalJsExport::class)
@file:DivaJsExport

package io.github.juevigrace.diva.lib.models.auth

import io.github.juevigrace.diva.core.DivaJsExport
import io.github.juevigrace.diva.lib.models.api.auth.signup.SignUpDto
import io.github.juevigrace.diva.lib.models.api.user.CreateUserDto
import io.github.juevigrace.diva.lib.models.session.SessionData
import kotlin.js.ExperimentalJsExport

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
            sessionData = sessionData.toSessionDataDto(),
        )
    }
}
