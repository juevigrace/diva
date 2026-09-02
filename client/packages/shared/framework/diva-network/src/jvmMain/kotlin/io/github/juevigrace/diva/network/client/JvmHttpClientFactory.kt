package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp

actual fun createDefaultHttpClient(
    block: HttpClientConfig<*>.() -> Unit,
): HttpClient = HttpClient(OkHttp) {
    defaultConfig()
    block()
}
