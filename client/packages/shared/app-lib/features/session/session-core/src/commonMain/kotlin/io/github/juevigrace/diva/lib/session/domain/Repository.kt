package io.github.juevigrace.diva.lib.session.domain

import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.lib.session.models.Session
import io.github.juevigrace.diva.lib.session.models.SessionStatus
import io.github.juevigrace.diva.lib.session.models.SessionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * A local session carries no server tokens, so its expiry timestamps are meaningless and the
 * time-based [Session.expired] check is skipped. [Session.status] still applies to every type.
 */
fun Session.ensureUsable(): Result<Session> = when {
    type == SessionType.LOCAL -> Result.success(this)
    expired -> Result.failure(IllegalStateException("Session $id is expired"))
    status != SessionStatus.ACTIVE -> Result.failure(IllegalStateException("Session $id is ${status.name}"))
    else -> Result.success(this)
}

inline fun <T> observeSession(
    crossinline sessionCall: suspend () -> Flow<Result<Session>>,
    crossinline onFound: suspend FlowCollector<Result<T>>.(session: Session) -> Unit,
): Flow<Result<T>> {
    return flow {
        sessionCall().collect { result ->
            result.fold(
                onFailure = { err -> emit(Result.failure(err)) },
                onSuccess = { session ->
                    session.ensureUsable().fold(
                        onFailure = { err -> emit(Result.failure(err)) },
                        onSuccess = { usable -> onFound(usable) },
                    )
                },
            )
        }
    }.flowOn(ioDispatcher)
}

inline fun <T> withSessionFlow(
    crossinline sessionCall: suspend () -> Result<Session>,
    crossinline onFound: suspend FlowCollector<Result<T>>.(session: Session) -> Unit,
): Flow<Result<T>> {
    return flow {
        sessionCall().fold(
            onFailure = { err -> emit(Result.failure(err)) },
            onSuccess = { session ->
                session.ensureUsable().fold(
                    onFailure = { err -> emit(Result.failure(err)) },
                    onSuccess = { usable -> onFound(usable) },
                )
            },
        )
    }.flowOn(ioDispatcher)
}

suspend fun <T> withSession(
    sessionCall: suspend () -> Result<Session>,
    onFound: suspend (session: Session) -> Result<T>,
): Result<T> {
    return withContext(ioDispatcher) {
        sessionCall().fold(
            onFailure = { err -> Result.failure(err) },
            onSuccess = { session ->
                session.ensureUsable().fold(
                    onFailure = { err -> Result.failure(err) },
                    onSuccess = { usable -> onFound(usable) },
                )
            },
        )
    }
}
