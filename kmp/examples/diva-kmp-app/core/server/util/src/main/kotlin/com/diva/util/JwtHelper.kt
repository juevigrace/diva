package com.diva.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.diva.models.auth.Session
import com.diva.models.session.SessionStatus
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.errors.toDivaError
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.getOrNull
import io.github.juevigrace.diva.core.isPresent
import io.github.juevigrace.diva.core.tryResult
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

class JwtHelper(
    private val secret: String,
    private val issuer: String,
    private val audience: String,
) {
    companion object {
        private const val ACCESS_EXPIRES_IN: String = "3600000ms"
        private const val REFRESH_EXPIRES_IN: String = "86400000ms"

        private const val USER_ID_CLAIM_KEY = "user_id"
        private const val SESSION_ID_CLAIM_KEY = "session_id"
    }

    val jwtVerifier: JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    fun createAccessToken(userId: String, sessionId: String): String = createJwt(userId, sessionId, ACCESS_EXPIRES_IN)

    fun createRefreshToken(userId: String, sessionId: String): String = createJwt(userId, sessionId, REFRESH_EXPIRES_IN)

    @OptIn(ExperimentalTime::class)
    private fun createJwt(
        userId: String,
        sessionId: String,
        expiresIn: String,
    ): String =
        JWT
            .create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(USER_ID_CLAIM_KEY, userId)
            .withClaim(SESSION_ID_CLAIM_KEY, sessionId)
            .withSubject(userId)
            .withExpiresAt(
                Clock.System
                    .now()
                    .plus(Duration.parse(expiresIn))
                    .toJavaInstant(),
            ).sign(Algorithm.HMAC256(secret))

    // TODO: REFACTOR
    suspend fun validate(
        credential: JWTCredential,
        sessionCallBack: suspend (sessionId: UUID) -> DivaResult<Option<Session>, DivaError>,
        onFound: suspend (session: Session) -> Unit,
    ): JWTPrincipal? {
        return tryResult(
            onError = { e ->
                println(e.toDivaError())
            }
        ) {
            val sessionId: String? = credential.payload.getClaim(SESSION_ID_CLAIM_KEY).asString()
            val userId: String? = credential.payload.getClaim(USER_ID_CLAIM_KEY).asString()

            val principal: JWTPrincipal? = when {
                sessionId == null -> null
                userId == null -> null
                else -> {
                    val parsedId = UUID.fromString(sessionId)
                    println("Session ID: $parsedId")
                    sessionCallBack(parsedId).fold(
                        onFailure = { err ->
                            println(err.toString())
                            null
                        },
                        onSuccess = { option ->
                            option.fold(
                                onNone = {
                                    println("Session not found")
                                    null
                                },
                                onSome = { session ->
                                    return when {
                                        session.user.deletedAt.isPresent() -> {
                                            null
                                        }
                                        else -> {
                                            when (session.status) {
                                                SessionStatus.ACTIVE -> {
                                                    onFound(session)
                                                    JWTPrincipal(credential.payload)
                                                }
                                                else -> null
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    )
                }
            }
            DivaResult.success(principal)
        }.getOrNull()
    }
}
