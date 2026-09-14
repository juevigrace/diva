package io.github.juevigrace.diva.network.client

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom

// ── URL helper ──

fun HttpRequestBuilder.setUrl(path: String, queryParams: Map<String, String> = emptyMap()) {
    if (path.startsWith("http")) {
        url.takeFrom(path)
    } else {
        url.appendPathSegments(path)
    }
    queryParams.forEach { (key, value) -> url.parameters.append(key, value) }
}
