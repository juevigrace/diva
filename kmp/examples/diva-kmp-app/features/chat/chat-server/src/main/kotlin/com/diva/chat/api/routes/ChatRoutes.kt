package com.diva.chat.api.routes

import com.diva.chat.api.handler.ChatHandler
import com.diva.models.api.chat.dtos.CreateChatDto
import com.diva.models.api.chat.dtos.UpdateChatDto
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.util.respond
import com.diva.util.respondBadRequest
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

// TODO: ADD SESSION AND PERMISSION CHECK TO ROUTES
fun Route.chatApiRoutes() {
    val handler: ChatHandler by inject()
    route("/chat") {
        authenticate(AUTH_JWT_KEY) {
            get {
                val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
                val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                handler.getChats(page, pageSize).respond(call)
            }
            route("/{id}") {
                get {
                    val id: String = call.pathParameters["id"]
                        ?: return@get call.respondBadRequest("missing chatId")
                    handler.getChat(id).respond(call)
                }
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing chatId")
                    handler.deleteChat(id).respond(call)
                }
            }
            post {
                val dto: CreateChatDto = call.receive()
                handler.createChat(dto).respond(call)
            }
            put {
                val dto: UpdateChatDto = call.receive()
                handler.updateChat(dto).respond(call)
            }
            chatParticipantApiRoutes()
            chatMessageApiRoutes()
        }
    }
}
