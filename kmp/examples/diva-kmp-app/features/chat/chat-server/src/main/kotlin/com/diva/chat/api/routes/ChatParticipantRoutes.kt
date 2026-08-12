package com.diva.chat.api.routes

import com.diva.chat.api.handler.ChatParticipantHandler
import com.diva.models.api.chat.dtos.AddParticipantDto
import com.diva.models.api.chat.dtos.DeleteParticipantDto
import com.diva.models.api.chat.dtos.UpdateParticipantDto
import com.diva.util.respond
import com.diva.util.respondBadRequest
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

internal fun Route.chatParticipantApiRoutes() {
    val handler: ChatParticipantHandler by inject()
    route("/participant") {
        get("/{chatId}") {
            val chatId: String = call.pathParameters["chatId"]
                ?: return@get call.respondBadRequest("missing chatId")

            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getChatParticipants(chatId, page, pageSize).respond(call)
        }
        post {
            val dto: AddParticipantDto = call.receive()
            handler.createChatParticipant(dto).respond(call)
        }
        put {
            val dto: UpdateParticipantDto = call.receive()
            handler.updateChatParticipant(dto).respond(call)
        }
        delete {
            val dto: DeleteParticipantDto = call.receive()
            handler.deleteChatParticipant(dto).respond(call)
        }
    }
}
