package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js

actual fun createDefaultHttpClient(
    block: HttpClientConfig<*>.() -> Unit,
): HttpClient = HttpClient(Js) {
    defaultConfig()
    block()
}
