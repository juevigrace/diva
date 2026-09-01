package io.github.juevigrace.diva.lib.core

import io.github.juevigrace.diva.core.ioDispatcher
import io.github.juevigrace.diva.lib.models.session.Session
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

// TODO: implement events for this?
// TODO: Implement error channel to avoid returning errors directly in the functions
//       and return the values from the cache instead but still log or notify an error
interface Repository {
    val scope: CoroutineScope
        get() = CoroutineScope(SupervisorJob() + ioDispatcher)

    fun <T> withSessionObserve(
        sessionCall: suspend () -> Flow<Result<Session>>,
        onFound: suspend FlowCollector<Result<T>>.(session: Session) -> Unit,
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

    fun <T> withSessionFlow(
        sessionCall: suspend () -> Result<Session>,
        onFound: suspend FlowCollector<Result<T>>.(session: Session) -> Unit,
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
}
