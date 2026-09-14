package io.github.juevigrace.diva.network.client

import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType

// ── GET ──

suspend fun DivaClient.get(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Get
        setUrl(path, queryParams)
        headers.forEach { header(it.key, it.value) }
    }
}

suspend inline fun <reified T : Any> DivaClient.getAs(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
): Result<T> {
    return get(path, queryParams, headers).map { it.body() }
}

// ── POST ──

suspend fun DivaClient.post(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Post
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        this.contentType(contentType)
        if (body != null) setBody(body)
    }
}

suspend inline fun <reified T : Any> DivaClient.postAs(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> {
    return post(path, body, headers, contentType).map { it.body() }
}

// ── PUT ──

suspend fun DivaClient.put(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Put
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        this.contentType(contentType)
        if (body != null) setBody(body)
    }
}

suspend inline fun <reified T : Any> DivaClient.putAs(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> {
    return put(path, body, headers, contentType).map { it.body() }
}

// ── PATCH ──

suspend fun DivaClient.patch(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Patch
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        this.contentType(contentType)
        if (body != null) setBody(body)
    }
}

suspend inline fun <reified T : Any> DivaClient.patchAs(
    path: String,
    body: Any? = null,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> {
    return patch(path, body, headers, contentType).map { it.body() }
}

// ── DELETE ──

suspend fun DivaClient.delete(
    path: String,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Delete
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        this.contentType(contentType)
    }
}

suspend fun DivaClient.delete(
    path: String,
    body: Any?,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<HttpResponse> {
    return call {
        method = HttpMethod.Delete
        setUrl(path)
        headers.forEach { header(it.key, it.value) }
        this.contentType(contentType)
        if (body != null) setBody(body)
    }
}

suspend inline fun <reified T : Any> DivaClient.deleteAs(
    path: String,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> {
    return delete(path, headers, contentType).map { it.body() }
}

suspend inline fun <reified T : Any> DivaClient.deleteAs(
    path: String,
    body: Any?,
    headers: Map<String, String> = emptyMap(),
    contentType: ContentType = ContentType.Application.Json,
): Result<T> {
    return delete(path, body, headers, contentType).map { it.body() }
}
