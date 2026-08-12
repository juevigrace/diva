package com.diva.verification.api.routes

import com.diva.models.api.user.dtos.EmailTokenDto
import com.diva.models.auth.Session
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.models.server.SESSION_KEY
import com.diva.util.respond
import com.diva.util.respondUnauthorized
import com.diva.verification.api.handler.VerificationHandler
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

// TODO: create flow for verification request
fun Route.verificationRoutes() {
    val handler: VerificationHandler by inject()
    route("/verify") {
        post {
            val dto: EmailTokenDto = call.receive()
            handler.verifyEmailToken(dto).respond(call)
        }
        authenticate(AUTH_JWT_KEY) {
            post("/email") {
                val session: Session = call.attributes.getOrNull(SESSION_KEY)
                    ?: return@post call.respondUnauthorized()
                val dto: EmailTokenDto = call.receive()
                handler.verifyUserEmail(dto, session).respond(call)
            }
        }
    }
}
