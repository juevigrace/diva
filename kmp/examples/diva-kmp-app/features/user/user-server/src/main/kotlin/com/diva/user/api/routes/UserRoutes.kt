package com.diva.user.api.routes

import com.diva.models.api.auth.dtos.PasswordUpdateDto
import com.diva.models.api.user.dtos.CreateUserDto
import com.diva.models.api.user.dtos.EmailTokenDto
import com.diva.models.api.user.dtos.UpdateUserDto
import com.diva.models.api.user.dtos.UserEmailDto
import com.diva.models.auth.Session
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.models.server.SESSION_KEY
import com.diva.user.api.handler.UserHandler
import com.diva.util.respond
import com.diva.util.respondBadRequest
import com.diva.util.respondUnauthorized
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userApiRoutes() {
    val handler: UserHandler by inject()
    route("/user") {
        get {
            val page: Int = call.queryParameters["page"]?.toIntOrNull() ?: 1
            val pageSize: Int = call.queryParameters["pageSize"]?.toIntOrNull() ?: 10
            handler.getUsers(page, pageSize).respond(call)
        }
        route("/{id}") {
            get {
                val idStr: String = call.pathParameters["id"]
                    ?: return@get call.respondBadRequest("missing userId")
                handler.getUser(idStr).respond(call)
            }
            authenticate(AUTH_JWT_KEY) {
                put {
                    val idStr: String = call.pathParameters["id"]
                        ?: return@put call.respondBadRequest("missing userId")
                    val session: Session = call.attributes.getOrNull(SESSION_KEY)
                        ?: return@put call.respondUnauthorized()
                    val dto: UpdateUserDto = call.receive()
                    handler.updateUser(idStr, dto, session).respond(call)
                }
                delete {
                    val idStr: String = call.pathParameters["id"]
                        ?: return@delete call.respondBadRequest("missing userId")
                    val session: Session = call.attributes.getOrNull(SESSION_KEY)
                        ?: return@delete call.respondUnauthorized()
                    handler.deleteUser(idStr, session).respond(call)
                }
            }
        }
        route("/forgot") {
            route("/password") {
                post("/request") {
                    val dto: UserEmailDto = call.receive()
                    handler.requestPasswordReset(dto).respond(call)
                }
                post("/confirm") {
                    val dto: EmailTokenDto = call.receive()
                    handler.confirmPasswordReset(dto).respond(call)
                }
                authenticate(AUTH_JWT_KEY) {
                    patch {
                        val session: Session = call.attributes.getOrNull(SESSION_KEY)
                            ?: return@patch call.respondUnauthorized()
                        val dto: PasswordUpdateDto = call.receive()
                        handler.resetPassword(dto, session)
                    }
                }
            }
        }
        authenticate(AUTH_JWT_KEY) {
            post {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@post call.respondUnauthorized()

                val dto: CreateUserDto = call.receive()
                handler.createUser(dto, session).respond(call)
            }
            userProfileRoutes()
            userPermissionsHandler()
        }
    }
}
