package com.diva.user.api.routes

import com.diva.models.api.ApiResponse
import com.diva.models.api.user.dtos.UpdateUserDto
import com.diva.models.api.user.dtos.UserEmailDto
import com.diva.models.api.user.preferences.dtos.UserPreferencesDto
import com.diva.models.auth.Session
import com.diva.models.server.SESSION_KEY
import com.diva.user.api.handler.UserHandler
import com.diva.util.respond
import com.diva.util.respondUnauthorized
import com.diva.util.sendResponse
import io.github.juevigrace.diva.core.network.HttpStatusCodes
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.uuid.ExperimentalUuidApi

internal fun Route.userProfileRoutes() {
    val handler: UserHandler by inject()
    route("/me") {
        @OptIn(ExperimentalUuidApi::class)
        put {
            val session: Session = call.attributes.getOrNull(SESSION_KEY)
                ?: return@put call.respondUnauthorized()
            val dto: UpdateUserDto = call.receive()
            handler.updateUser(session.id.toString(), dto, session).respond(call)
        }
        route("/email") {
            post("/request") {
                call.sendResponse(
                    ApiResponse<Unit>(
                        statusCode = HttpStatusCodes.NotImplemented.code,
                        message = "Not implemented"
                    )
                )
            }
            post("/confirm") {
                call.sendResponse<Unit>(
                    ApiResponse(
                        statusCode = HttpStatusCodes.NotImplemented.code,
                        message = "Not implemented"
                    )
                )
            }
            patch {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@patch call.respondUnauthorized()
                val dto: UserEmailDto = call.receive()
                handler.updateEmail(dto, session).respond(call)
            }
        }
        @OptIn(ExperimentalUuidApi::class)
        delete {
            val session: Session = call.attributes.getOrNull(SESSION_KEY)
                ?: return@delete call.respondUnauthorized()
            handler.deleteUser(session.id.toString(), session).respond(call)
        }
        route("/preferences") {
            post {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@post call.respondUnauthorized()
                val dto: UserPreferencesDto = call.receive()
                handler.createPreferences(dto, session).respond(call)
            }
            put {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@put call.respondUnauthorized()
                val dto: UserPreferencesDto = call.receive()
                handler.updatePreferences(dto, session).respond(call)
            }
        }
    }
}
