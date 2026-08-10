package com.diva.verification.api.client

import com.diva.models.api.user.dtos.EmailTokenDto
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError

interface VerificationNetworkClient {
    suspend fun verifyEmailToken(dto: EmailTokenDto): DivaResult<Unit, DivaError>
    suspend fun verifyUserEmail(dto: EmailTokenDto, token: String): DivaResult<Unit, DivaError>
}
