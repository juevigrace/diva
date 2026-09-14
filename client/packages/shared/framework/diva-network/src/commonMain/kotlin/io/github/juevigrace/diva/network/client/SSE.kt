package io.github.juevigrace.diva.network.client

import io.github.juevigrace.diva.core.ioDispatcher
import io.ktor.client.plugins.sse.ClientSSESession
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


// ── SSE ──

suspend inline fun DivaClient.sse(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
    context: CoroutineContext = ioDispatcher,
    crossinline block: suspend ClientSSESession.() -> Unit,
): Result<Unit> {
    return withContext(context) {
        runCatching {
            client.sse(
                request = {
                    method = HttpMethod.Get
                    setUrl(path, queryParams)
                    headers.forEach { header(it.key, it.value) }
                }
            ) {
                block()
            }
        }
    }
}
