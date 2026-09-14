package io.github.juevigrace.diva.network.client

import io.github.juevigrace.diva.core.ioDispatcher
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.header
import io.ktor.http.HttpMethod
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

// ── WebSocket ──

suspend inline fun DivaClient.webSocket(
    path: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap(),
    context: CoroutineContext = ioDispatcher,
    crossinline block: suspend DefaultClientWebSocketSession.() -> Unit,
): Result<Unit> {
    return withContext(context) {
        runCatching {
            client.webSocket(
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
