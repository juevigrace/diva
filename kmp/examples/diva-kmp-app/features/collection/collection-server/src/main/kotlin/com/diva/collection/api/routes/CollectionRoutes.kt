package com.diva.collection.api.routes

import com.diva.collection.api.handler.CollectionHandler
import com.diva.models.api.collection.dtos.CreateCollectionDto
import com.diva.models.api.collection.dtos.DeleteCollectionMedia
import com.diva.models.api.collection.dtos.UpdateCollectionDto
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

fun Route.collectionApiRoutes() {
    val handler: CollectionHandler by inject()
    route("/collection") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getCollections(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val id: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing collectionId")
                handler.getCollection(id).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing collectionId")
                    handler.deleteCollection(id).respond(call)
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: CreateCollectionDto = call.receive()
                handler.createCollection(dto).respond(call)
            }
            put {
                val dto: UpdateCollectionDto = call.receive()
                handler.updateCollection(dto).respond(call)
            }
        }
        route("/media") {
            get("/{collectionId}") {
                val collectionId: String = call.pathParameters["collectionId"]
                    ?: return@get call.respondBadRequest("missing collectionId")
                val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
                val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
                handler.getCollectionMedia(collectionId, page, pageSize).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val dto: DeleteCollectionMedia = call.receive()
                    handler.deleteCollectionMedia(dto)
                }
            }
        }
        playlistApiRoutes()
    }
}
