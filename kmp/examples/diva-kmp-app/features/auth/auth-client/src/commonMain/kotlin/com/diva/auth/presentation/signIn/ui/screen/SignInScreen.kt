package com.diva.auth.presentation.signIn.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.auth.presentation.signIn.events.SignInEvents
import com.diva.auth.presentation.signIn.state.SignInState
import com.diva.auth.presentation.signIn.viewmodel.SignInViewModel
import com.diva.core.resources.Res
import com.diva.core.resources.dont_have_account
import com.diva.core.resources.email_or_username
import com.diva.core.resources.facebook
import com.diva.core.resources.forgot_password
import com.diva.core.resources.google
import com.diva.core.resources.hide_password
import com.diva.core.resources.ic_eye
import com.diva.core.resources.ic_eye_off
import com.diva.core.resources.ic_facebook
import com.diva.core.resources.ic_google
import com.diva.core.resources.ic_lock
import com.diva.core.resources.ic_user
import com.diva.core.resources.ic_x
import com.diva.core.resources.logo
import com.diva.core.resources.or_continue_with
import com.diva.core.resources.password
import com.diva.core.resources.password_placeholder
import com.diva.core.resources.puerro
import com.diva.core.resources.show_password
import com.diva.core.resources.sign_in
import com.diva.core.resources.sign_up
import com.diva.core.resources.twitter
import com.diva.core.resources.username_placeholder
import com.diva.ui.models.SocialProvider
import com.diva.ui.navigation.arguments.ForgotAction
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.ui.components.layout.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = koinViewModel()
) {
    val state: SignInState by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Screen { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 24.dp)
        ) {
            item {
                Image(
                    modifier = Modifier.size(200.dp),
                    painter = painterResource(Res.drawable.puerro),
                    contentDescription = stringResource(Res.string.logo)
                )
            }

            item {
                OutlinedTextField(
                    value = state.signInForm.username,
                    onValueChange = { viewModel.onEvent(SignInEvents.OnUsernameChanged(it)) },
                    label = { Text(stringResource(Res.string.email_or_username)) },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.username_placeholder),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    isError = state.formValidation.usernameError is Option.Some,
                    supportingText = when (val error = state.formValidation.usernameError) {
                        is Option.Some -> { { Text(stringResource(error.value)) } }
                        is Option.None -> null
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
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
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = state.signInForm.password,
                        onValueChange = { viewModel.onEvent(SignInEvents.OnPasswordChanged(it)) },
                        label = { Text(stringResource(Res.string.password)) },
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.password_placeholder),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        },
                        isError = state.formValidation.passwordError is Option.Some,
                        supportingText = when (val error = state.formValidation.passwordError) {
                            is Option.Some -> { { Text(stringResource(error.value)) } }
                            is Option.None -> null
                        },
                        singleLine = true,
                        visualTransformation = if (state.showPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                if (state.submitEnabled) {
                                    viewModel.onEvent(SignInEvents.OnSubmit)
                                }
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
                            IconButton(onClick = { viewModel.onEvent(SignInEvents.TogglePassword) }) {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(
                                        if (state.showPassword) Res.drawable.ic_eye_off else Res.drawable.ic_eye
                                    ),
                                    contentDescription = if (state.showPassword) {
                                        stringResource(
                                            Res.string.hide_password
                                        )
                                    } else {
                                        stringResource(Res.string.show_password)
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.clickable(
                                onClick = {
                                    viewModel.onEvent(SignInEvents.OnForgot(ForgotAction.OnForgotPassword))
                                }
                            ).padding(4.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.forgot_password),
                                autoSize = TextAutoSize.StepBased(
                                    minFontSize = MaterialTheme.typography.labelMedium.fontSize,
                                    maxFontSize = MaterialTheme.typography.bodyLarge.fontSize
                                ),
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                            )
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.onEvent(SignInEvents.OnSubmit) },
                    enabled = state.submitEnabled && !state.submitLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (state.submitLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(Res.string.sign_in))
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f))
                    Text(
                        text = stringResource(Res.string.or_continue_with),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f))
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.socialProviders.forEach { provider ->
                        OutlinedButton(
                            onClick = { viewModel.onEvent(SignInEvents.OnSocialLogin(provider)) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                painter = painterResource(
                                    when (provider) {
                                        SocialProvider.Google -> Res.drawable.ic_google
                                        SocialProvider.Facebook -> Res.drawable.ic_facebook
                                        SocialProvider.Twitter -> Res.drawable.ic_x
                                    }
                                ),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (provider) {
                                    SocialProvider.Google -> stringResource(Res.string.google)
                                    SocialProvider.Facebook -> stringResource(Res.string.facebook)
                                    SocialProvider.Twitter -> stringResource(Res.string.twitter)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.dont_have_account),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = { viewModel.onEvent(SignInEvents.OnSignUp) }
                    ) {
                        Text(stringResource(Res.string.sign_up))
                    }
                }
            }
        }
    }
}
