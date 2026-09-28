package io.github.juevigrace.diva.lib.session.domain

import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.lib.session.models.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

inline fun <T> observeSession(
    crossinline sessionCall: suspend () -> Flow<Result<Session>>,
    crossinline onFound: suspend FlowCollector<Result<T>>.(session: Session) -> Unit,
): Flow<Result<T>> {
    return flow {
        sessionCall().collect { result ->
            result.fold(
                onFailure = { err -> emit(Result.failure(err)) },
                onSuccess = { session ->
                    onFound(session)
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
                onFound(session)
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
            onSuccess = { session -> onFound(session) },
        )
    }
}