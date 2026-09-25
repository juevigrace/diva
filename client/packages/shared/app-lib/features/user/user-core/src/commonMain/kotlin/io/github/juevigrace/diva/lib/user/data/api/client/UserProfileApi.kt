package io.github.juevigrace.diva.lib.user.data.api.client

import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.toOption
import io.github.juevigrace.diva.lib.models.api.ApiResponse
import io.github.juevigrace.diva.lib.models.user.api.profile.CreateProfileDto
import io.github.juevigrace.diva.lib.models.user.api.profile.UpdateProfileDto
import io.github.juevigrace.diva.lib.models.user.api.profile.UserProfileResponse
import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.FormDataContent
import io.github.juevigrace.diva.network.client.getAs
import io.github.juevigrace.diva.network.client.multipartPatchAs
import io.github.juevigrace.diva.network.client.postAs
import io.github.juevigrace.diva.network.client.putAs
import io.ktor.http.ContentType

interface UserProfileApi {
    suspend fun get(uid: String, token: String): Result<Option<UserProfileResponse>>
    suspend fun create(uid: String, dto: CreateProfileDto, token: String): Result<Unit>
    suspend fun update(uid: String, dto: UpdateProfileDto, token: String): Result<Unit>
    suspend fun updateAvatar(
        uid: String,
        bytes: ByteArray,
        fileName: String,
        contentType: ContentType,
        token: String,
    ): Result<Unit>
}

class UserProfileApiImpl(
    private val client: DivaClient,
) : UserProfileApi {
    override suspend fun get(uid: String, token: String): Result<Option<UserProfileResponse>> {
        return client.getAs<ApiResponse<UserProfileResponse?>>(
            path = "/api/user/$uid/profile",
            headers = mapOf("Authorization" to "Bearer $token"),
        ).map { it.data.toOption() }
    }

    override suspend fun create(uid: String, dto: CreateProfileDto, token: String): Result<Unit> {
        return client.postAs<Unit>(
            path = "/api/user/$uid/profile",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun update(uid: String, dto: UpdateProfileDto, token: String): Result<Unit> {
        return client.putAs<Unit>(
            path = "/api/user/$uid/profile",
            body = dto,
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    override suspend fun updateAvatar(
        uid: String,
        bytes: ByteArray,
        fileName: String,
        contentType: ContentType,
        token: String,
    ): Result<Unit> {
        return client.multipartPatchAs<Unit>(
            path = "/api/user/$uid/profile/avatar",
            formData = listOf(
                FormDataContent.FileItem(
                    key = "avatar",
                    bytes = bytes,
                    fileName = fileName,
                    contentType = contentType,
                ),
            ),
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
