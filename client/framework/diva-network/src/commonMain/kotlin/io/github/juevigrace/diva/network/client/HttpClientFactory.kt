package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun createDefaultHttpClient(
    block: HttpClientConfig<*>.() -> Unit = {},
): HttpClient
