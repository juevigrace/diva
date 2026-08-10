package com.diva.social.api.routes

import com.diva.models.api.social.post.dtos.CreatePostDto
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.social.api.handler.PostHandler
import com.diva.util.respond
import com.diva.util.respondBadRequest
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.postApiRoutes() {
    val handler: PostHandler by inject()
    route("/post") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getPosts(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val id: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing postId")
                handler.getPostById(id).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                // TODO: restrict deletion
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing postId")
                    handler.deletePost(id).respond(call)
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: CreatePostDto = call.receive()
                handler.createPost(dto).respond(call)
            }
        }
        route("/attachment") {
            get("/{postId}") {
                val postId: String = call.pathParameters["postId"]
                    ?: return@get call.respondBadRequest("missing postId")
                handler.getPostAttachments(postId).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete("/{id}") {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing postId")
                    handler.deletePostAttachment(id).respond(call)
                }
            }
        }
    }
}
