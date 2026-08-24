package io.github.juevigrace.diva.network.client

import io.ktor.client.call.body
import io.ktor.client.plugins.sse.ClientSSESession
import io.ktor.client.plugins.sse.sse
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.takeFrom

// ── GET ──

suspend fun DivaClient.get(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> = call {
    method = HttpMethod.Get
    setUrl(path, queryParams)
    headers.forEach { header(it.key, it.value) }
}

suspend inline fun <reified T : Any> DivaClient.getAs(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Result<T> = get(path, queryParams, headers).map { it.body() }

// ── POST ──

suspend fun DivaClient.post(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> = call {
    method = HttpMethod.Post
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    this.contentType(contentType)
    if (body != null) setBody(body)
}

suspend inline fun <reified T : Any> DivaClient.postAs(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> = post(path, body, headers, contentType).map { it.body() }

// ── PUT ──

suspend fun DivaClient.put(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> = call {
    method = HttpMethod.Put
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    this.contentType(contentType)
    if (body != null) setBody(body)
}

suspend inline fun <reified T : Any> DivaClient.putAs(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> = put(path, body, headers, contentType).map { it.body() }

// ── PATCH ──

suspend fun DivaClient.patch(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> = call {
    method = HttpMethod.Patch
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    this.contentType(contentType)
    if (body != null) setBody(body)
}

suspend inline fun <reified T : Any> DivaClient.patchAs(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> = patch(path, body, headers, contentType).map { it.body() }

// ── DELETE ──

suspend fun DivaClient.delete(
    path: String,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> = call {
    method = HttpMethod.Delete
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    this.contentType(contentType)
}

suspend fun DivaClient.delete(
    path: String,
    body: Any?,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> = call {
    method = HttpMethod.Delete
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    this.contentType(contentType)
    if (body != null) setBody(body)
}

// ── SSE ──

suspend inline fun DivaClient.sse(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
    crossinline block: suspend ClientSSESession.() -> Unit,
): Result<Unit> = try {
    client.sse(
        request = {
            method = HttpMethod.Get
            setUrl(path, queryParams)
            headers.forEach { header(it.key, it.value) }
        }
    ) {
        block()
    }
    Result.success(Unit)
} catch (e: Exception) {
    Result.failure(e)
}

// ── WebSocket ──

suspend inline fun DivaClient.webSocket(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
    crossinline block: suspend DefaultClientWebSocketSession.() -> Unit,
): Result<Unit> = try {
    client.webSocket(
        request = {
            method = HttpMethod.Get
            setUrl(path, queryParams)
            headers.forEach { header(it.key, it.value) }
        }
    ) {
        block()
    }
    Result.success(Unit)
} catch (e: Exception) {
    Result.failure(e)
}

// ── Multipart POST ──

suspend fun DivaClient.multipartPost(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> = call {
    method = HttpMethod.Post
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    setBody(
        MultiPartFormDataContent(
            formData {
                formData.forEach { item ->
                    when (item) {
                        is FormDataContent.FormItem -> append(item.key, item.value)
                        is FormDataContent.FileItem -> append(
                            item.key,
                            item.bytes,
                            io.ktor.http.Headers.build {
                                append(HttpHeaders.ContentType, item.contentType.toString())
                                append(HttpHeaders.ContentDisposition, "filename=${item.fileName}")
                            },
                        )
                    }
                }
            }
        )
    )
}

suspend inline fun <reified T : Any> DivaClient.multipartPostAs(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<T> = multipartPost(path, formData, headers).map { it.body() }

// ── Multipart PUT ──

suspend fun DivaClient.multipartPut(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> = call {
    method = HttpMethod.Put
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    setBody(
        MultiPartFormDataContent(
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
    )
}

suspend inline fun <reified T : Any> DivaClient.multipartPutAs(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<T> = multipartPut(path, formData, headers).map { it.body() }

// ── Multipart PATCH ──

suspend fun DivaClient.multipartPatch(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> = call {
    method = HttpMethod.Patch
    setUrl(path)
    headers.forEach { header(it.key, it.value) }
    setBody(
        MultiPartFormDataContent(
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
    )
}

suspend inline fun <reified T : Any> DivaClient.multipartPatchAs(
    path: String,
    formData: List<FormDataContent>,
    headers: Map<String, String> = emptyMap(),
): Result<T> = multipartPatch(path, formData, headers).map { it.body() }

// ── URL helper ──

fun HttpRequestBuilder.setUrl(path: String, queryParams: Map<String, String> = emptyMap()) {
    if (path.startsWith("http")) {
        url.takeFrom(path)
    } else {
        url.appendPathSegments(path)
    }
    queryParams.forEach { (key, value) -> url.parameters.append(key, value) }
}
