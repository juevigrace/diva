package io.github.juevigrace.diva.network.client

import io.github.juevigrace.diva.core.ioDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal class DivaClientImpl(
    override var client: HttpClient,
) : DivaClient {

    override val scope: CoroutineScope = CoroutineScope(ioDispatcher)

    override fun config(block: HttpClientConfig<*>.() -> Unit) {
        client = client.config(block)
    }

    override suspend fun call(
        context: CoroutineContext,
        builder: HttpRequestBuilder.() -> Unit,
    ): Result<HttpResponse> {
        return withContext(context) {
            runCatching {
                client.request(builder)
            }
        }
    }
}
