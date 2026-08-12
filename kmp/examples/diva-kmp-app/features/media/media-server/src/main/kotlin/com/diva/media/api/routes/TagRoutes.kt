package com.diva.media.api.routes

import com.diva.media.api.handler.TagHandler
import com.diva.models.api.media.dtos.CreateTagDto
import com.diva.models.api.media.dtos.UpdateTagDto
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

fun Route.tagApiRoutes() {
    val handler: TagHandler by inject()
    route("/tag") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getTags(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val id: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing tagId")
                handler.getTag(id).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing tagId")
                    handler.deleteTag(id).respond(call)
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: CreateTagDto = call.receive()
                handler.createTag(dto).respond(call)
            }
            put {
                val id: String = call.pathParameters["id"]
                    ?: return@put call.respondBadRequest("missing tagId")
                val dto: UpdateTagDto = call.receive()
                handler.updateTag(dto).respond(call)
            }
        }
    }
}
