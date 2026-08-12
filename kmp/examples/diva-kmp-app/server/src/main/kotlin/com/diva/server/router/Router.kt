package com.diva.server.router

import com.diva.auth.api.routes.authRoutes
import com.diva.chat.api.routes.chatApiRoutes
import com.diva.collection.api.routes.collectionApiRoutes
import com.diva.database.DivaDB
import com.diva.media.api.routes.mediaApiRoutes
import com.diva.media.api.routes.tagApiRoutes
import com.diva.models.api.ApiResponse
import com.diva.permissions.api.routes.permissionsApiRoutes
import com.diva.social.api.routes.postApiRoutes
import com.diva.user.api.routes.userApiRoutes
import com.diva.util.respondInternalServerError
import com.diva.verification.api.routes.verificationRoutes
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.database.DivaDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.sse.sse
import io.ktor.server.websocket.webSocket
import io.ktor.sse.ServerSentEvent
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import org.koin.ktor.ext.inject

fun Routing.applicationRoutes() {
    val db: DivaDatabase<DivaDB> by inject()
    sse("/see/hello") {
        send(ServerSentEvent("world"))
    }
    webSocket("/ws") { // websocketSession
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val text = frame.readText()
                outgoing.send(Frame.Text("YOU SAID: $text"))
                if (text.equals("bye", ignoreCase = true)) {
                    close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
                }
            }
        }
    }
    get("/health") {
        db.checkHealth().fold(
            onFailure = { err ->
                call.respondInternalServerError(err.message)
            },
            onSuccess = { call.respond(HttpStatusCode.OK, ApiResponse(data = it, message = "OK")) }
        )
    }
    route("/api") {
        authRoutes()
        chatApiRoutes()
        collectionApiRoutes()
        mediaApiRoutes()
        permissionsApiRoutes()
        postApiRoutes()
        tagApiRoutes()
        userApiRoutes()
        verificationRoutes()
    }
}
