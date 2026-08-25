package io.github.juevigrace.diva.network.client

import io.github.juevigrace.diva.core.ioDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

interface DivaClient {
    val client: HttpClient
    val scope: CoroutineScope

    fun config(block: HttpClientConfig<*>.() -> Unit)

    suspend fun call(
        context: CoroutineContext = ioDispatcher,
        builder: HttpRequestBuilder.() -> Unit,
    ): Result<HttpResponse>

    companion object {
        fun create(
            client: HttpClient,
        ): DivaClient {
            return DivaClientImpl(client)
        }

        fun create(
            block: HttpClientConfig<*>.() -> Unit = {},
        ): DivaClient {
            return DivaClientImpl(createDefaultHttpClient(block))
        }

        fun create(): DivaClient {
            return DivaClientImpl(createDefaultHttpClient())
        }
    }
}
