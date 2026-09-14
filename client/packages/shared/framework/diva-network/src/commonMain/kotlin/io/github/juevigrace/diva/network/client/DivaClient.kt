package io.github.juevigrace.diva.network.client

import io.github.juevigrace.diva.core.ioDispatcher
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
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
            return DivaClientImpl(
                client = createDefaultHttpClient {
                    defaultConfig()
                    install(ContentNegotiation) {
                        json(
                            Json {
                                encodeDefaults = true
                                prettyPrint = true
                            }
                        )
                    }
                }
            )
        }
    }
}

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
