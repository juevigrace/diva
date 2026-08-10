package com.diva.media.api.routes

import com.diva.media.api.handler.MediaHandler
import com.diva.models.api.media.dtos.CreateMediaDto
import com.diva.models.api.media.dtos.DeleteMediaTagDto
import com.diva.models.api.media.dtos.UpdateMediaDto
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

fun Route.mediaApiRoutes() {
    val handler: MediaHandler by inject()
    route("/media") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getMedia(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val id: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing mediaId")
                handler.getMedia(id).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing mediaId")
                    handler.deleteMedia(id).respond(call)
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: CreateMediaDto = call.receive()
                handler.createMedia(dto).respond(call)
            }
            put {
                val dto: UpdateMediaDto = call.receive()
                handler.updateMedia(dto).respond(call)
            }
        }
        route("/tag") {
            get("/{mediaId}") {
                val mediaId: String = call.pathParameters["mediaId"]
                    ?: return@get call.respondBadRequest("missing mediaId")
                handler.getMediaTagsByMediaId(mediaId).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val dto: DeleteMediaTagDto = call.receive()
                    handler.deleteMediaTag(dto).respond(call)
                }
            }
        }
    }
}
