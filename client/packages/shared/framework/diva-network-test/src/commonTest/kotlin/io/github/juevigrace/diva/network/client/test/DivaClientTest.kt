package io.github.juevigrace.diva.network.client.test

import io.github.juevigrace.diva.network.client.DivaClient
import io.github.juevigrace.diva.network.client.FormDataContent
import io.github.juevigrace.diva.network.client.defaultConfig
import io.github.juevigrace.diva.network.client.get
import io.github.juevigrace.diva.network.client.multipartPatch
import io.github.juevigrace.diva.network.client.multipartPost
import io.github.juevigrace.diva.network.client.multipartPut
import io.github.juevigrace.diva.network.client.post
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DivaClientTest {

    @Test
    fun getReturnsMockedResponse() = runTest {
        val client = testClient {
            respond(
                content = """{"data":true}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val response = client.get(path = "/ping").getOrThrow()

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getReturnsResponseWithServerErrorCode() = runTest {
        val client = testClient {
            respond(
                content = """{"message":"error"}""",
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val result = client.get(path = "/ping")

        assertTrue(result.isSuccess)
        assertEquals(HttpStatusCode.InternalServerError, result.getOrThrow().status)
    }

    @Test
    fun getReturnsFailureWhenEngineFails() = runTest {
        val client = testClient {
            throw IllegalStateException("Connection refused")
        }

        val result = client.get(path = "/ping")

        assertTrue(result.isFailure)
    }

    @Test
    fun postSendsBodyAndReturnsResponse() = runTest {
        val client = testClient {
            respond(
                content = """{"id":1,"username":"john"}""",
                status = HttpStatusCode.Created,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val response = client.post(
            path = "/user",
            body = UserResponse(id = 1, username = "john"),
        ).getOrThrow()

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun configAppliesNewPlugins() = runTest {
        val requests = mutableListOf<String>()
        val client = DivaClient.create(
            HttpClient(MockEngine) {
                defaultConfig()
                engine {
                    addHandler { request ->
                        requests.add(request.url.toString())
                        respond(
                            content = "ok",
                            status = HttpStatusCode.OK,
                        )
                    }
                }
            }
        )

        client.get(path = "/data").getOrThrow()
        assertEquals(1, requests.size)

        client.config {
            defaultRequest {
                url("http://example.com")
            }
        }

        client.get(path = "/data").getOrThrow()
        assertEquals(2, requests.size)
    }

    @Test
    fun multipartPostSendsFormData() = runTest {
        var capturedMethod: HttpMethod? = null
        val client = testClient {
            capturedMethod = it.method
            respond(
                content = """{"ok":true}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val response = client.multipartPost(
            path = "/upload",
            formData = listOf(
                FormDataContent.FormItem("description", "test file"),
                FormDataContent.FileItem(
                    key = "file",
                    bytes = byteArrayOf(1, 2, 3),
                    fileName = "test.bin",
                    contentType = ContentType.Application.OctetStream,
                ),
            ),
        ).getOrThrow()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(HttpMethod.Post, capturedMethod)
    }

    @Test
    fun multipartPutSendsFormData() = runTest {
        var capturedMethod: HttpMethod? = null
        val client = testClient {
            capturedMethod = it.method
            respond(
                content = """{"ok":true}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val response = client.multipartPut(
            path = "/upload",
            formData = listOf(
                FormDataContent.FormItem("key", "value"),
            ),
        ).getOrThrow()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(HttpMethod.Put, capturedMethod)
    }

    @Test
    fun multipartPatchSendsFormData() = runTest {
        var capturedMethod: HttpMethod? = null
        val client = testClient {
            capturedMethod = it.method
            respond(
                content = """{"ok":true}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        }

        val response = client.multipartPatch(
            path = "/upload",
            formData = listOf(
                FormDataContent.FormItem("key", "value"),
            ),
        ).getOrThrow()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(HttpMethod.Patch, capturedMethod)
    }

    private fun testClient(handler: MockRequestHandler): DivaClient {
        return DivaClient.create(
            HttpClient(MockEngine) {
                defaultConfig()
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        encodeDefaults = true
                    })
                }
                engine {
                    addHandler(handler)
                }
            }
        )
    }
}

@Serializable
data class PingResponse(val data: Boolean)

@Serializable
data class UserResponse(val id: Int, val username: String)

@Serializable
data class ErrorResponse(val message: String)
