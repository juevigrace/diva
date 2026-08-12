package com.diva.auth.presentation.forgot.viewmodel

import com.diva.auth.presentation.forgot.events.ForgotEvents
import com.diva.auth.presentation.forgot.state.ForgotState
import com.diva.auth.presentation.forgot.state.ForgotStep
import com.diva.core.resources.Res
import com.diva.core.resources.field_required
import com.diva.core.resources.password_mismatch
import com.diva.core.resources.password_reset_success
import com.diva.core.resources.verification_code_sent
import com.diva.ui.messages.toToast
import com.diva.ui.navigation.Destination
import com.diva.ui.navigation.SignInDestination
import com.diva.ui.navigation.arguments.ForgotAction
import com.diva.user.data.UserRepository
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.core.fold
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.toast.ToastMessage
import io.github.juevigrace.diva.ui.toast.Toaster
import io.github.juevigrace.diva.ui.viewmodel.DivaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class ForgotViewModel(
    private val action: ForgotAction,
    private val userRepository: UserRepository,
    private val navigator: Navigator<Destination>,
    private val toaster: Toaster,
) : DivaViewModel() {
    private val _state = MutableStateFlow(ForgotState())
    val state: StateFlow<ForgotState> = _state.asStateFlow()

    fun onEvent(event: ForgotEvents) {
        when (event) {
            is ForgotEvents.OnEmailChanged -> emailChanged(event.email)
            is ForgotEvents.OnTokenChanged -> tokenChanged(event.token)
            is ForgotEvents.OnNewPasswordChanged -> newPasswordChanged(event.password)
            is ForgotEvents.OnConfirmPasswordChanged -> confirmPasswordChanged(event.password)
            ForgotEvents.OnSubmit -> submit()
            ForgotEvents.OnBack -> handleBack()
            ForgotEvents.OnNavigateToSignIn -> navigateToSignIn()
        }
    }

    private fun emailChanged(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    private fun tokenChanged(token: String) {
        _state.update { it.copy(token = token, tokenError = null) }
    }

    private fun newPasswordChanged(password: String) {
        _state.update { it.copy(newPassword = password, passwordError = null) }
    }

    private fun confirmPasswordChanged(password: String) {
        _state.update { it.copy(confirmPassword = password, passwordError = null) }
    }

    private fun handleBack() {
        when (val currentStep = _state.value.currentStep) {
            ForgotStep.EMAIL_INPUT -> navigator.pop()
            ForgotStep.TOKEN_INPUT -> _state.update { it.copy(currentStep = ForgotStep.EMAIL_INPUT) }
            ForgotStep.NEW_PASSWORD -> _state.update { it.copy(currentStep = ForgotStep.TOKEN_INPUT) }
        }
    }

    private fun navigateToSignIn() {
        navigator.replaceAll(SignInDestination)
    }

    private fun submit() {
        val currentState = _state.value
        when (currentState.currentStep) {
            ForgotStep.EMAIL_INPUT -> requestPasswordReset()
            ForgotStep.TOKEN_INPUT -> confirmPasswordReset()
            ForgotStep.NEW_PASSWORD -> resetPassword()
        }
    }

    private fun requestPasswordReset() {
        val email = _state.value.email
        if (email.isBlank()) {
            scope.launch {
                val errorMsg = getString(Res.string.field_required)
                _state.update { it.copy(emailError = errorMsg) }
            }
            return
        }

        _state.update { it.copy(loading = true) }

        scope.launch {
            userRepository.forgotPasswordRequest(email).collect { result ->
                result.fold(
                    onFailure = { err ->
                        _state.update { it.copy(loading = false, emailError = err.toToast().message) }
                    },
                    onSuccess = {
                        _state.update { it.copy(loading = false, currentStep = ForgotStep.TOKEN_INPUT) }
                        val message = getString(Res.string.verification_code_sent)
                        toaster.show(ToastMessage(message = message, isError = false, details = Option.None))
                    }
                )
            }
        }
    }

    private fun confirmPasswordReset() {
        val token = _state.value.token
        if (token.isBlank()) {
            scope.launch {
                val errorMsg = getString(Res.string.field_required)
                _state.update { it.copy(tokenError = errorMsg) }
            }
            return
        }

        _state.update { it.copy(loading = true) }

        scope.launch {
            userRepository.forgotPasswordConfirm(token).collect { result ->
                result.fold(
                    onFailure = { err ->
                        _state.update { it.copy(loading = false, tokenError = err.toToast().message) }
                    },
                    onSuccess = {
                        _state.update { it.copy(loading = false, currentStep = ForgotStep.NEW_PASSWORD) }
                    }
                )
            }
        }
    }

    private fun resetPassword() {
        val newPassword = _state.value.newPassword
        val confirmPassword = _state.value.confirmPassword

        if (newPassword.isBlank()) {
            scope.launch {
                val errorMsg = getString(Res.string.field_required)
                _state.update { it.copy(passwordError = errorMsg) }
            }
            return
        }

        if (newPassword != confirmPassword) {
            scope.launch {
                val errorMsg = getString(Res.string.password_mismatch)
                _state.update { it.copy(passwordError = errorMsg) }
            }
            return
        }

        _state.update { it.copy(loading = true) }

        scope.launch {
            userRepository.forgotPasswordReset(newPassword).collect { result ->
                result.fold(
                    onFailure = { err ->
                        _state.update { it.copy(loading = false, passwordError = err.toToast().message) }
                    },
                    onSuccess = {
                        _state.update { it.copy(loading = false, success = true) }
                        val message = getString(Res.string.password_reset_success)
                        toaster.show(ToastMessage(message = message, isError = false, details = Option.None))
                        navigator.navigate(SignInDestination)
                    }
                )
            }
        }
    }
}
