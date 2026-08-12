package com.diva.auth.presentation.forgot.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.auth.presentation.forgot.events.ForgotEvents
import com.diva.auth.presentation.forgot.state.ForgotState
import com.diva.auth.presentation.forgot.state.ForgotStep
import com.diva.auth.presentation.forgot.viewmodel.ForgotViewModel
import com.diva.core.resources.Res
import com.diva.core.resources.already_have_account
import com.diva.core.resources.confirm_password
import com.diva.core.resources.confirm_password_placeholder
import com.diva.core.resources.email
import com.diva.core.resources.email_placeholder
import com.diva.core.resources.forgot_password
import com.diva.core.resources.go_back
import com.diva.core.resources.hide_password
import com.diva.core.resources.ic_chevron_left
import com.diva.core.resources.ic_eye
import com.diva.core.resources.ic_eye_off
import com.diva.core.resources.ic_lock
import com.diva.core.resources.ic_user
import com.diva.core.resources.next
import com.diva.core.resources.password
import com.diva.core.resources.password_placeholder
import com.diva.core.resources.reset_password
import com.diva.core.resources.send_verification_code
import com.diva.core.resources.show_password
import com.diva.core.resources.sign_in
import com.diva.core.resources.verification_code
import com.diva.core.resources.verification_code_placeholder
import com.diva.ui.navigation.arguments.ForgotAction
import io.github.juevigrace.diva.ui.components.layout.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotScreen(
    action: ForgotAction,
    viewModel: ForgotViewModel = koinViewModel(parameters = { parametersOf(action) })
) {
    val state: ForgotState by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Screen(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(Res.string.reset_password))
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(ForgotEvents.OnBack) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_left),
                            contentDescription = stringResource(Res.string.go_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (state.currentStep) {
                ForgotStep.EMAIL_INPUT -> {
                    EmailInputStep(
                        email = state.email,
                        emailError = state.emailError,
                        loading = state.loading,
                        onEmailChanged = { viewModel.onEvent(ForgotEvents.OnEmailChanged(it)) },
                        onSubmit = { viewModel.onEvent(ForgotEvents.OnSubmit) },
                        onNavigateToSignIn = { viewModel.onEvent(ForgotEvents.OnNavigateToSignIn) },
                        focusManager = focusManager
                    )
                }
                ForgotStep.TOKEN_INPUT -> {
                    TokenInputStep(
                        token = state.token,
                        tokenError = state.tokenError,
                        loading = state.loading,
                        onTokenChanged = { viewModel.onEvent(ForgotEvents.OnTokenChanged(it)) },
                        onSubmit = { viewModel.onEvent(ForgotEvents.OnSubmit) },
                        focusManager = focusManager
                    )
                }
                ForgotStep.NEW_PASSWORD -> {
                    NewPasswordStep(
                        newPassword = state.newPassword,
                        confirmPassword = state.confirmPassword,
                        passwordError = state.passwordError,
                        loading = state.loading,
                        passwordVisible = passwordVisible,
                        confirmPasswordVisible = confirmPasswordVisible,
                        onNewPasswordChanged = { viewModel.onEvent(ForgotEvents.OnNewPasswordChanged(it)) },
                        onConfirmPasswordChanged = { viewModel.onEvent(ForgotEvents.OnConfirmPasswordChanged(it)) },
                        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
                        onToggleConfirmPasswordVisibility = { confirmPasswordVisible = !confirmPasswordVisible },
                        onSubmit = { viewModel.onEvent(ForgotEvents.OnSubmit) },
                        focusManager = focusManager
                    )
                }
            }
        }
    }
}

@Composable
private fun EmailInputStep(
    email: String,
    emailError: String?,
    loading: Boolean,
    onEmailChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Text(
        text = stringResource(Res.string.forgot_password),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        text = "Enter your email address to receive a verification code",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 32.dp)
    )

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChanged,
        label = { Text(stringResource(Res.string.email)) },
        placeholder = {
            Text(
                text = stringResource(Res.string.email_placeholder),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        },
        isError = emailError != null,
        supportingText = emailError?.let { { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSubmit()
            }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_user),
                contentDescription = null
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onSubmit,
        enabled = !loading && email.isNotBlank(),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(stringResource(Res.string.send_verification_code))
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.already_have_account),
            style = MaterialTheme.typography.bodyMedium
        )
        TextButton(onClick = onNavigateToSignIn) {
            Text(stringResource(Res.string.sign_in))
        }
    }
}

@Composable
private fun TokenInputStep(
    token: String,
    tokenError: String?,
    loading: Boolean,
    onTokenChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Text(
        text = stringResource(Res.string.verification_code),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        text = "Enter the verification code sent to your email",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 32.dp)
    )

    OutlinedTextField(
        value = token,
        onValueChange = onTokenChanged,
        label = { Text(stringResource(Res.string.verification_code)) },
        placeholder = {
            Text(
                text = stringResource(Res.string.verification_code_placeholder),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        },
        isError = tokenError != null,
        supportingText = tokenError?.let { { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSubmit()
            }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_eye_off),
                contentDescription = null
            )
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onSubmit,
        enabled = !loading && token.isNotBlank(),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(stringResource(Res.string.next))
        }
    }
}

@Composable
private fun NewPasswordStep(
    newPassword: String,
    confirmPassword: String,
    passwordError: String?,
    loading: Boolean,
    passwordVisible: Boolean,
    confirmPasswordVisible: Boolean,
    onNewPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onToggleConfirmPasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Text(
        text = stringResource(Res.string.reset_password),
        style = MaterialTheme.typography.headlineLarge,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Text(
        text = "Enter your new password",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 32.dp)
    )

    OutlinedTextField(
        value = newPassword,
        onValueChange = onNewPasswordChanged,
        label = { Text(stringResource(Res.string.password)) },
        placeholder = {
            Text(
                text = stringResource(Res.string.password_placeholder),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        },
        isError = passwordError != null,
        supportingText = passwordError?.let { { Text(it) } },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = onTogglePasswordVisibility) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(
                        if (passwordVisible) Res.drawable.ic_eye_off else Res.drawable.ic_eye
                    ),
                    contentDescription = if (passwordVisible) {
                        stringResource(Res.string.hide_password)
                    } else {
                        stringResource(Res.string.show_password)
                    }
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = onConfirmPasswordChanged,
        label = { Text(stringResource(Res.string.confirm_password)) },
        placeholder = {
            Text(
                text = stringResource(Res.string.confirm_password_placeholder),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        },
        isError = passwordError != null,
        singleLine = true,
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onSubmit()
            }
        ),
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_lock),
                contentDescription = null
            )
        },
        trailingIcon = {
            IconButton(onClick = onToggleConfirmPasswordVisibility) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(
                        if (confirmPasswordVisible) Res.drawable.ic_eye_off else Res.drawable.ic_eye
                    ),
                    contentDescription = if (confirmPasswordVisible) {
                        stringResource(Res.string.hide_password)
                    } else {
                        stringResource(Res.string.show_password)
                    }
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onSubmit,
        enabled = !loading && newPassword.isNotBlank() && confirmPassword.isNotBlank(),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(stringResource(Res.string.reset_password))
        }
    }
}
