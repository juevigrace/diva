package com.diva.auth.api.routes

import com.diva.auth.api.handler.AuthHandler
import com.diva.models.api.ApiResponse
import com.diva.models.api.auth.dtos.SessionDataDto
import com.diva.models.api.auth.dtos.SignInDto
import com.diva.models.api.auth.dtos.SignUpDto
import com.diva.models.auth.Session
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.models.server.SESSION_KEY
import com.diva.util.respond
import com.diva.util.respondUnauthorized
import com.diva.util.sendResponse
import io.github.juevigrace.diva.core.network.HttpStatusCodes
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val handler: AuthHandler by inject()
    route("/auth") {
        post("/signIn") {
            val dto: SignInDto = call.receive()
            handler.signIn(dto).respond(call)
        }
        post("/signUp") {
            val dto: SignUpDto = call.receive()
            handler.signUp(dto).respond(call)
        }
        authenticate(AUTH_JWT_KEY) {
            post("/signOut") {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@post call.respondUnauthorized()
                handler.signOut(session).respond(call)
            }
            post("/ping") {
                call.attributes.getOrNull(SESSION_KEY)
                    ?: return@post call.respondUnauthorized()
                call.sendResponse(ApiResponse<Unit>(statusCode = HttpStatusCodes.OK.code, message = "Pong"))
            }
            post("/refresh") {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@post call.respondUnauthorized()
                val dto: SessionDataDto = call.receive()
                handler.refresh(dto, session).respond(call)
            }
        }
    }
}
