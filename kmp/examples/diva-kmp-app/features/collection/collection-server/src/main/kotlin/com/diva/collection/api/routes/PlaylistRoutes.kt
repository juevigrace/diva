package com.diva.collection.api.routes

import com.diva.collection.api.handler.PlaylistHandler
import com.diva.models.api.collection.dtos.PlaylistDto
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
fun Route.playlistApiRoutes() {
    val handler: PlaylistHandler by inject()
    route("/playlist") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getPlaylists(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val id: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing playlistId")
                handler.getPlaylist(id).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing playlistId")
                    handler.deletePlaylist(id).respond(call)
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: PlaylistDto = call.receive()
                handler.createPlaylist(dto).respond(call)
            }
            put {
                val dto: PlaylistDto = call.receive()
                handler.updatePlaylist(dto).respond(call)
            }
        }
        playlistContributorApiRoutes()
        playlistSuggestionsApiRoutes()
    }
}
