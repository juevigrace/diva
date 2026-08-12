package com.diva.auth.forgot.data

import com.diva.auth.data.api.client.AuthApi
import com.diva.auth.session.data.SessionRepository
import com.diva.models.Repository
import com.diva.models.actions.Actions
import com.diva.models.api.auth.forgot.password.dtos.ForgotPasswordConfirmDto
import com.diva.models.api.auth.forgot.password.dtos.UpdatePasswordDto
import com.diva.models.api.auth.session.dtos.SessionDataDto
import com.diva.models.auth.Session
import com.diva.models.config.AppConfig
import com.diva.models.user.actions.UserAction
import com.diva.ui.navigation.arguments.ForgotAction
import com.diva.user.api.client.UserApi
import com.diva.user.data.actions.UserActionsRepository
import io.github.juevigrace.diva.core.errors.ConstraintException
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.core.getOrElse
import io.github.juevigrace.diva.core.util.logError
import kotlin.fold
import kotlin.uuid.ExperimentalUuidApi

interface ForgotRepository : Repository {
    suspend fun forgotPasswordReset(newPassword: String): Result<Unit>
    suspend fun checkForAction(action: ForgotAction): Result<UserAction>
}

class ForgotRepositoryImpl(
    private val authClient: AuthApi,
    private val sessionRepository: SessionRepository,
    private val uaRepository: UserActionsRepository,
    private val userClient: UserApi,
    private val config: AppConfig,
) : ForgotRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun forgotPasswordReset(newPassword: String): Result<Unit> {
        val action = uaRepository.getAction(Actions.PASSWORD_RESET)
            .getOrElse { err -> return Result.failure(err) }

        return authClient.forgotPasswordConfirm(
            dto = ForgotPasswordConfirmDto(
                id = action.id.toString(),
                sessionData = SessionDataDto(
                    device = config.deviceName,
                    userAgent = config.agent
                )
            )
        ).fold(
            onFailure = { Result.failure(it) },
            onSuccess = { res ->
                sessionRepository.newSession(Session.fromResponse(res)).fold(
                    onFailure = { Result.failure(it) },
                    onSuccess = {
                        userClient.updatePassword(
                            id = res.userId,
                            dto = UpdatePasswordDto(newPassword = newPassword),
                            token = res.accessToken
                        ).fold(
                            onFailure = { Result.failure(it) },
                            onSuccess = {
                                uaRepository.deleteByAction(Actions.PASSWORD_RESET).onFailure { err ->
                                    logError(this::class.simpleName ?: "ForgotRepository", err.toString())
                                }

                                sessionRepository.logoutTemporal().onFailure { err ->
                                    logError(this::class.simpleName ?: "ForgotRepository", err.toString())
                                }
                            }
                        )
                    }
                )
            }
        )
    }

    override suspend fun checkForAction(action: ForgotAction): Result<UserAction> {
        return when (action) {
            ForgotAction.Password, ForgotAction.PasswordWithAuth ->
                uaRepository.getAction(Actions.PASSWORD_RESET)
            ForgotAction.Unspecified -> Result.failure(
                ConstraintException(
                    field = "action",
                    constraint = "missing",
                    value = "unspecified"
                )
            )
        }
    }
}
