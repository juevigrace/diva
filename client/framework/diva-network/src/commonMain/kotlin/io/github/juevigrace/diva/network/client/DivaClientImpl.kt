package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse

internal class DivaClientImpl(
    override var client: HttpClient,
) : DivaClient {

    override fun config(block: HttpClientConfig<*>.() -> Unit) {
        client = client.config(block)
    }

    override suspend fun call(builder: HttpRequestBuilder.() -> Unit): Result<HttpResponse> {
        return runCatching { client.request(builder) }
    }
}
