package com.diva.auth.data

import com.diva.auth.api.client.AuthNetworkClient
import com.diva.database.session.SessionStorage
import com.diva.models.Repository
import com.diva.models.actions.Actions
import com.diva.models.actions.AppActions
import com.diva.models.actions.toAction
import com.diva.models.api.auth.dtos.SessionDataDto
import com.diva.models.auth.Session
import com.diva.models.auth.SignInForm
import com.diva.models.auth.SignUpForm
import io.github.juevigrace.diva.core.DivaResult
import io.github.juevigrace.diva.core.errors.DivaError
import io.github.juevigrace.diva.core.onFailure
import io.github.juevigrace.diva.core.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi

interface AuthRepository : Repository {
    fun signIn(form: SignInForm): Flow<DivaResult<Map<Actions, AppActions>, DivaError>>
    fun signUp(form: SignUpForm): Flow<DivaResult<Map<Actions, AppActions>, DivaError>>
    fun signOut(): Flow<DivaResult<Unit, DivaError>>
    fun ping(): Flow<DivaResult<Unit, DivaError>>
    fun refresh(device: String): Flow<DivaResult<Unit, DivaError>>
}

class AuthRepositoryImpl(
    private val sessionStorage: SessionStorage,
    private val authClient: AuthNetworkClient,
) : AuthRepository {
    @OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
    override fun signIn(form: SignInForm): Flow<DivaResult<Map<Actions, AppActions>, DivaError>> {
        return flow {
            authClient.signIn(form.toSignInDto())
                .onFailure { err -> emit(DivaResult.failure(err)) }
                .onSuccess { res ->
                    val session = Session.fromResponse(res.session)
                    sessionStorage
                        .insert(session)
                        .onFailure { err -> emit(DivaResult.failure(err)) }
                        .onSuccess {
                            sessionStorage.update(session.copy(isCurrent = true))
                                .onFailure { err ->
                                    sessionStorage.delete(session.id)
                                        .onFailure { err -> println("panik: ${err.message}") }
                                    emit(DivaResult.failure(err))
                                }
                            val actions = res.actions.map { aRes -> aRes.toAction() }
                            emit(
                                DivaResult.success(actions.associateWith { AppActions.fromAction(it) })
                            )
                        }
                }
        }.flowOn(Dispatchers.Default)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun signUp(form: SignUpForm): Flow<DivaResult<Map<Actions, AppActions>, DivaError>> {
        return flow {
            authClient.signUp(form.toSignUpDto())
                .onFailure { err -> emit(DivaResult.failure(err)) }
                .onSuccess { res ->
                    val session = Session.fromResponse(res.session)
                    sessionStorage
                        .insert(session)
                        .onFailure { err -> emit(DivaResult.failure(err)) }
                        .onSuccess {
                            sessionStorage.update(session.copy(isCurrent = true))
                                .onFailure { err ->
                                    sessionStorage.delete(session.id)
                                        .onFailure { err -> println("panik: ${err.message}") }
                                    emit(DivaResult.failure(err))
                                }
                            val actions = res.actions.map { aRes -> aRes.toAction() }
                            emit(
                                DivaResult.success(actions.associateWith { AppActions.fromAction(it) })
                            )
                        }
                }
        }.flowOn(Dispatchers.Default)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun signOut(): Flow<DivaResult<Unit, DivaError>> {
        return withSession(sessionStorage::getCurrentSession) { value ->
            authClient.signOut(value.accessToken)
                .onFailure { err -> emit(DivaResult.failure(err)) }
                .onSuccess {
                    sessionStorage.delete(value.id)
                        .onFailure { err -> emit(DivaResult.failure(err)) }
                        .onSuccess { emit(DivaResult.success(Unit)) }
                }
        }
    }

    override fun ping(): Flow<DivaResult<Unit, DivaError>> {
        return withSession(sessionStorage::getCurrentSession) { value ->
            authClient.ping(value.accessToken)
                // TODO: this should refresh if fails for the access token
                .onFailure { err -> emit(DivaResult.failure(err)) }
                .onSuccess { emit(DivaResult.success(Unit)) }
        }
    }

    override fun refresh(device: String): Flow<DivaResult<Unit, DivaError>> {
        return withSession(sessionStorage::getCurrentSession) { value ->
            authClient.refresh(SessionDataDto(device), value.refreshToken)
                .onFailure { err -> emit(DivaResult.failure(err)) }
                .onSuccess { res ->
                    // TODO: this is wrong?
                    sessionStorage
                        .insert(Session.fromResponse(res))
                        .onFailure { err -> emit(DivaResult.failure(err)) }
                        .onSuccess { emit(DivaResult.success(Unit)) }
                }
        }
    }
}
