package io.github.juevigrace.diva.lib.verification.data.api.client

import io.github.juevigrace.diva.lib.models.verification.api.RequestActionVerificationDto
import io.github.juevigrace.diva.lib.models.verification.api.VerifyActionDto
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.postAs

interface VerificationApi {
    suspend fun requestVerification(dto: RequestActionVerificationDto): Result<Unit>
    suspend fun verify(dto: VerifyActionDto): Result<Unit>
}

class VerificationApiImpl(
    private val client: DivaClient,
) : VerificationApi {
    override suspend fun requestVerification(dto: RequestActionVerificationDto): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/verification/request",
            body = dto,
        )
    }

    override suspend fun verify(dto: VerifyActionDto): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/verification",
            body = dto,
        )
    }
}