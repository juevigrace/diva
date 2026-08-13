package io.github.juevigrace.diva.network.client.test

import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.defaultConfig
import io.github.juevigrace.diva.network.client.factory.JvmDivaClientFactory
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import java.io.IOException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DivaClientTest {
    @Test
    fun `get returns mocked response`() = runBlocking {
        val client = client { request ->
            respondJson(PingResponse(data = true))
        }

        val response = client.get(path = "/ping").getOrThrow()

        assertEquals(HttpMethod.Get, response.call.request.method)
        assertEquals(HttpStatusCode.OK, response.status)
        val body: PingResponse = response.body()
        assertTrue(body.data)
    }

    @Test
    fun `get returns response with server error status`() = runBlocking {
        val client = client { request ->
            respondJson(ErrorResponse(message = "error"), HttpStatusCode.InternalServerError)
        }

        val result = client.get(path = "/ping")

        assertTrue(result.isSuccess)
        assertEquals(HttpStatusCode.InternalServerError, result.getOrThrow().status)
    }

    @Test
    fun `get returns failure when engine fails`() = runBlocking {
        val client = client { request ->
            throw IOException("Connection refused")
        }

        val result = client.get(path = "/ping")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() != null)
    }

    @Test
    fun `get routes to handler by path`() = runBlocking {
        val client = client { request ->
            when (request.url.encodedPath) {
                "/ping" -> respondJson(PingResponse(data = true))
                else -> respondJson(ErrorResponse(message = "not found"), HttpStatusCode.NotFound)
            }
        }

        val response = client.get(path = "/ping").getOrThrow()

        assertEquals(HttpStatusCode.OK, response.status)
        val body: PingResponse = response.body()
        assertTrue(body.data)
    }

    @Test
    fun `post sends typed body and returns typed response`() = runBlocking {
        val client = client { request ->
            respondJson(UserResponse(id = 1, username = "john"), HttpStatusCode.Created)
        }

        val response = client.post(
            path = "/user",
            body = UserResponse(id = 1, username = "john"),
            headers = emptyMap(),
            contentType = ContentType.Application.Json,
            serializer = UserResponse.serializer()
        ).getOrThrow()

        assertEquals(HttpMethod.Post, response.call.request.method)
        assertEquals(HttpStatusCode.Created, response.status)
        val body: UserResponse = response.body()
        assertEquals(UserResponse(id = 1, username = "john"), body)
    }

    private fun client(handler: MockRequestHandler): DivaClient {
        return JvmDivaClientFactory(MockEngine) {
            defaultConfig()
            engine {
                addHandler(handler)
            }
        }.create()
    }

    private inline fun <reified T> MockRequestHandleScope.respondJson(
        body: T,
        status: HttpStatusCode = HttpStatusCode.OK
    ): HttpResponseData = respond(
        content = Json.encodeToString(body),
        status = status,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )
}

@Serializable
data class PingResponse(
    val data: Boolean
)

@Serializable
data class UserResponse(
    val id: Int,
    val username: String
)

@Serializable
data class ErrorResponse(
    val message: String
)
