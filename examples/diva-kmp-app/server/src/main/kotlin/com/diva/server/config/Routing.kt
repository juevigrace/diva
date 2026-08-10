package com.diva.server.config

import com.diva.server.router.applicationRoutes
import com.diva.server.validation.appRequestValidation
import com.diva.util.respondBadRequest
import com.diva.util.respondInternalServerError
import com.diva.util.respondNotFound
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.routing.routing
import io.ktor.server.sse.SSE
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlin.time.Duration.Companion.seconds

fun Application.configureRouting() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { _ ->
            call.respondNotFound(
                if (call.request.path().startsWith("/api")) {
                    "not found"
                } else {
                    "page not found"
                }
            )
        }
        exception<Exception> { call, cause ->
            call.respondInternalServerError("error: ${cause.message}")
        }

        exception<RequestValidationException> { call, cause ->
            call.respondBadRequest(cause.reasons.joinToString())
        }
    }
    install(SSE)
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    install(RequestValidation) {
        appRequestValidation()
    }
    routing {
        applicationRoutes()
        staticResources("/", "static")
    }
}
