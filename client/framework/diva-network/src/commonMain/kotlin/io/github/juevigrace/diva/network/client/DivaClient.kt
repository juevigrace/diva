package io.github.juevigrace.diva.network.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse

interface DivaClient {
    val client: HttpClient

    fun config(block: HttpClientConfig<*>.() -> Unit)

    suspend fun call(builder: HttpRequestBuilder.() -> Unit): Result<HttpResponse>

    companion object {
        fun create(client: HttpClient): DivaClient = DivaClientImpl(client)

        fun create(
            block: HttpClientConfig<*>.() -> Unit = {},
        ): DivaClient = DivaClientImpl(createDefaultHttpClient(block))

        fun create(): DivaClient = DivaClientImpl(createDefaultHttpClient())
    }
}
