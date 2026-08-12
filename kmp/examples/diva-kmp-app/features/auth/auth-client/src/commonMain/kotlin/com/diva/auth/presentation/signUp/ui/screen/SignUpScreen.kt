package com.diva.auth.presentation.signUp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.diva.auth.presentation.signUp.events.SignUpEvents
import com.diva.auth.presentation.signUp.state.SignUpState
import com.diva.auth.presentation.signUp.viewmodel.SignUpViewModel
import com.diva.core.resources.Res
import com.diva.core.resources.alias
import com.diva.core.resources.alias_placeholder
import com.diva.core.resources.already_have_account
import com.diva.core.resources.birth_date
import com.diva.core.resources.cancel
import com.diva.core.resources.confirm_password
import com.diva.core.resources.confirm_password_placeholder
import com.diva.core.resources.email
import com.diva.core.resources.email_placeholder
import com.diva.core.resources.hide_password
import com.diva.core.resources.ic_calendar_event
import com.diva.core.resources.ic_eye
import com.diva.core.resources.ic_eye_off
import com.diva.core.resources.ic_lock
import com.diva.core.resources.ic_mail
import com.diva.core.resources.ic_phone
import com.diva.core.resources.ic_user
import com.diva.core.resources.logo
import com.diva.core.resources.ok
import com.diva.core.resources.password
import com.diva.core.resources.password_placeholder
import com.diva.core.resources.phone
import com.diva.core.resources.phone_placeholder
import com.diva.core.resources.privacy_policy
import com.diva.core.resources.puerro
import com.diva.core.resources.show_password
import com.diva.core.resources.sign_in
import com.diva.core.resources.sign_up
import com.diva.core.resources.terms_and_conditions
import com.diva.core.resources.username
import com.diva.core.resources.username_placeholder
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.ui.components.layout.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = koinViewModel()
) {
    val state: SignUpState by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val datePickerInteractionSource = remember { MutableInteractionSource() }
    val isDatePickerPressed by datePickerInteractionSource.collectIsPressedAsState()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (state.signUpForm.birthDate > 0) state.signUpForm.birthDate else null,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= Clock.System.now().toEpochMilliseconds()
            }
        }
    )

    LaunchedEffect(isDatePickerPressed) {
        if (isDatePickerPressed) {
            viewModel.onEvent(SignUpEvents.ToggleDatePicker)
        }
    }

    if (state.showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(SignUpEvents.ToggleDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onEvent(SignUpEvents.OnBirthDateChanged(it))
                        }
                        viewModel.onEvent(SignUpEvents.ToggleDatePicker)
                    }
                ) {
                    Text(stringResource(Res.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SignUpEvents.ToggleDatePicker) }) {
                    Text(stringResource(Res.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Screen { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
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
                    value = state.signUpForm.email,
                    onValueChange = { viewModel.onEvent(SignUpEvents.OnEmailChanged(it)) },
                    label = { Text("${stringResource(Res.string.email)} *") },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.email_placeholder),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    isError = state.formValidation.emailError is Option.Some,
                    supportingText = when (val error = state.formValidation.emailError) {
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
                            painter = painterResource(Res.drawable.ic_mail),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = state.signUpForm.username,
                    onValueChange = { viewModel.onEvent(SignUpEvents.OnUsernameChanged(it)) },
                    label = { Text("${stringResource(Res.string.username)} *") },
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
                        keyboardType = KeyboardType.Text,
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
                OutlinedTextField(
                    value = state.signUpForm.alias,
                    onValueChange = { viewModel.onEvent(SignUpEvents.OnAliasNameChanged(it)) },
                    label = { Text(stringResource(Res.string.alias)) },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.alias_placeholder),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
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
                OutlinedTextField(
                    value = state.signUpForm.phone,
                    onValueChange = { viewModel.onEvent(SignUpEvents.OnPhoneChanged(it)) },
                    label = { Text(stringResource(Res.string.phone)) },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.phone_placeholder),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    isError = state.formValidation.phoneError is Option.Some,
                    supportingText = when (val error = state.formValidation.phoneError) {
                        is Option.Some -> { { Text(stringResource(error.value)) } }
                        is Option.None -> null
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Res.drawable.ic_phone),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = state.formattedBirthDate,
                    onValueChange = {},
                    label = { Text("${stringResource(Res.string.birth_date)} *") },
                    isError = state.formValidation.birthDateError is Option.Some,
                    supportingText = when (val error = state.formValidation.birthDateError) {
                        is Option.Some -> { { Text(stringResource(error.value)) } }
                        is Option.None -> null
                    },
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Res.drawable.ic_calendar_event),
                            contentDescription = null
                        )
                    },
                    interactionSource = datePickerInteractionSource,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = state.signUpForm.password,
                    onValueChange = { viewModel.onEvent(SignUpEvents.OnPasswordChanged(it)) },
                    label = { Text("${stringResource(Res.string.password)} *") },
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
                    visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        IconButton(onClick = { viewModel.onEvent(SignUpEvents.TogglePasswordVisibility) }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(
                                    if (state.passwordVisible) Res.drawable.ic_eye_off else Res.drawable.ic_eye
                                ),
                                contentDescription = if (state.passwordVisible) {
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
            }

            item {
                OutlinedTextField(
                    value = state.signUpForm.confirmPassword,
                    onValueChange = { viewModel.onEvent(SignUpEvents.OnConfirmPasswordChanged(it)) },
                    label = { Text("${stringResource(Res.string.confirm_password)} *") },
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.confirm_password_placeholder),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    },
                    singleLine = true,
                    visualTransformation = if (state.confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (state.submitEnabled) {
                                viewModel.onEvent(SignUpEvents.OnSubmit)
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
                        IconButton(onClick = { viewModel.onEvent(SignUpEvents.ToggleConfirmPasswordVisibility) }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(
                                    if (state.confirmPasswordVisible) Res.drawable.ic_eye_off else Res.drawable.ic_eye
                                ),
                                contentDescription = if (state.confirmPasswordVisible) {
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
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.signUpForm.termsAndConditions,
                        onCheckedChange = { viewModel.onEvent(SignUpEvents.ToggleTerms) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { viewModel.onEvent(SignUpEvents.ToggleTerms) }) {
                        Text(
                            text = stringResource(Res.string.terms_and_conditions),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.signUpForm.privacyPolicy,
                        onCheckedChange = { viewModel.onEvent(SignUpEvents.TogglePrivacyPolicy) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { viewModel.onEvent(SignUpEvents.TogglePrivacyPolicy) }) {
                        Text(
                            text = stringResource(Res.string.privacy_policy),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.onEvent(SignUpEvents.OnSubmit) },
                    enabled = state.submitEnabled && !state.submitLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (state.submitLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(Res.string.sign_up))
                    }
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.already_have_account),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    TextButton(
                        onClick = { viewModel.onEvent(SignUpEvents.OnNavigateToSignIn) }
                    ) {
                        Text(stringResource(Res.string.sign_in))
                    }
                }
            }
        }
    }
}
