package com.diva.server.config

import com.diva.database.session.SessionStorage
import com.diva.models.server.AUTH_JWT_KEY
import com.diva.models.server.SESSION_KEY
import com.diva.util.JwtHelper
import com.diva.util.respondUnauthorized
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.toKotlinUuid

@OptIn(ExperimentalUuidApi::class)
fun Application.configureSecurity() {
    val secret: String = environment.config.property("jwt.secret").getString()
    val issuer: String = environment.config.property("jwt.issuer").getString()
    val audience: String = environment.config.property("jwt.audience").getString()
    val jwtRealm: String = environment.config.property("jwt.realm").getString()
    val jwtHelper: JwtHelper by inject { parametersOf(secret, issuer, audience) }
    val sessionStorage: SessionStorage by inject()

    // TODO: create a verification middleware for authentication or just restrict access for non verified users
    authentication {
        jwt(AUTH_JWT_KEY) {
            realm = jwtRealm
            verifier(jwtHelper.jwtVerifier)
            validate { credential ->
                jwtHelper.validate(
                    credential = credential,
                    sessionCallBack = { id ->
                        sessionStorage.getById(id.toKotlinUuid())
                    },
                    onFound = { session ->
                        attributes[SESSION_KEY] = session
                    }
                )
            }
            challenge { _, _ ->
                call.respondUnauthorized()
            }
        }
    }
}
