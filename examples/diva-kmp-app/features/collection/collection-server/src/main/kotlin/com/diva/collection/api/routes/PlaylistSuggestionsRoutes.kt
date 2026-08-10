package com.diva.collection.api.routes

import com.diva.collection.api.handler.PlaylistSuggestionHandler
import com.diva.models.api.collection.dtos.CreatePlaylistSuggestionDto
import com.diva.models.api.collection.dtos.UpdatePlaylistSuggestionDto
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.util.respond
import com.diva.util.respondBadRequest
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

internal fun Route.playlistSuggestionsApiRoutes() {
    val handler: PlaylistSuggestionHandler by inject()
    route("/suggestions") {
        get("/{playlistId}") {
            val playlistId: String = call.pathParameters["playlistId"]
                ?: return@get call.respondBadRequest("missing playlistId")
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getPlaylistSuggestions(playlistId, page, pageSize).respond(call)
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: CreatePlaylistSuggestionDto = call.receive()
                handler.createPlaylistSuggestion(dto).respond(call)
            }
            put {
                val dto: UpdatePlaylistSuggestionDto = call.receive()
                handler.updatePlaylistSuggestion(dto).respond(call)
            }
            delete("/{id}") {
                val id: String = call.pathParameters["id"]
                    ?: return@delete call.respondBadRequest("missing playlistSuggestionId")
                handler.deletePlaylistSuggestion(id).respond(call)
            }
        }
    }
}
