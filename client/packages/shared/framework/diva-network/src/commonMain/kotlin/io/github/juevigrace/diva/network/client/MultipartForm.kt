package io.github.juevigrace.diva.network.client

import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

sealed interface FormDataContent {
    data class FormItem(val key: String, val value: String) : FormDataContent

    data class FileItem(
        val key: String,
        val bytes: ByteArray,
        val fileName: String,
        val contentType: ContentType,
    ) : FormDataContent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is FileItem) return false
            return key == other.key &&
                bytes.contentEquals(other.bytes) &&
                fileName == other.fileName &&
                contentType == other.contentType
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + bytes.contentHashCode()
            result = 31 * result + fileName.hashCode()
            result = 31 * result + contentType.hashCode()
            return result
        }
    }
}

// ── Multipart POST ──

suspend fun DivaClient.multipartPost(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Post
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        setBody(buildFormDataContent(formData))
    }
}

suspend inline fun <reified T : Any> DivaClient.multipartPostAs(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<T> {
    return multipartPost(path, formData, headers).map { it.body() }
}

// ── Multipart PUT ──

suspend fun DivaClient.multipartPut(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Put
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        setBody(buildFormDataContent(formData))
    }
}

suspend inline fun <reified T : Any> DivaClient.multipartPutAs(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<T> {
    return multipartPut(path, formData, headers).map { it.body() }
}

// ── Multipart PATCH ──

suspend fun DivaClient.multipartPatch(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Patch
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        setBody(buildFormDataContent(formData))
    }
}

suspend inline fun <reified T : Any> DivaClient.multipartPatchAs(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<T> {
    return multipartPatch(path, formData, headers).map { it.body() }
}

private fun buildFormDataContent(formData: List<FormDataContent>): MultiPartFormDataContent {
    return MultiPartFormDataContent(
        formData {
            formData.forEach { item ->
                when (item) {
                    is FormDataContent.FormItem -> append(item.key, item.value)
                    is FormDataContent.FileItem -> append(
                        item.key,
                        item.bytes,
                        Headers.build {
                            append(HttpHeaders.ContentType, item.contentType.toString())
                            append(HttpHeaders.ContentDisposition, "filename=${item.fileName}")
                        },
                    )
                }
            }
        }
    )
}
