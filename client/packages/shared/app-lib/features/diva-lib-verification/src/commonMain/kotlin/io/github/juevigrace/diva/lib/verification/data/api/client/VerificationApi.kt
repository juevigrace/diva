package io.github.juevigrace.diva.lib.verification.data.api.client

import io.github.juevigrace.diva.lib.models.api.verification.RequestActionVerificationDto
import io.github.juevigrace.diva.lib.models.api.verification.VerifyActionDto
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.post

interface VerificationApi {
    suspend fun requestVerification(dto: RequestActionVerificationDto): Result<Unit>
    suspend fun verify(dto: VerifyActionDto): Result<Unit>
}

class VerificationApiImpl(
    private val client: DivaClient,
) : VerificationApi {
    override suspend fun requestVerification(dto: RequestActionVerificationDto): Result<Unit> {
        return client.post(
            path = "/api/verification/request",
            body = dto,
        ).map { }
    }

    override suspend fun verify(dto: VerifyActionDto): Result<Unit> {
        return client.post(
            path = "/api/verification",
            body = dto,
        ).map { }
    }
}