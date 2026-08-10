package com.diva.permissions.api.routes

import com.diva.models.api.permissions.dtos.CreatePermissionDto
import com.diva.models.api.permissions.dtos.UpdatePermissionDto
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.permissions.api.handler.PermissionsHandler
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

fun Route.permissionsApiRoutes() {
    val handler: PermissionsHandler by inject()
    route("/permissions") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getPermissions(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val id: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing permissionId")
                handler.getPermissionById(id).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                delete {
                    val id: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing permissionId")
                    handler.deletePermission(id).respond(call)
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val dto: CreatePermissionDto = call.receive()
                handler.createPermission(dto).respond(call)
            }
            put {
                val dto: UpdatePermissionDto = call.receive()
                handler.updatePermission(dto).respond(call)
            }
        }
    }
}
