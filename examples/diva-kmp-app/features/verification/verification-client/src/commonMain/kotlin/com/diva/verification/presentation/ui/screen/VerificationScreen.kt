package com.diva.verification.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diva.core.resources.Res
import com.diva.core.resources.go_back
import com.diva.core.resources.ic_chevron_left
import com.diva.core.resources.ic_mail
import com.diva.core.resources.verification_code
import com.diva.core.resources.verification_code_description
import com.diva.core.resources.verification_code_placeholder
import com.diva.core.resources.verify
import com.diva.ui.navigation.arguments.VerificationAction
import com.diva.verification.presentation.events.VerificationEvents
import com.diva.verification.presentation.viewmodel.VerificationViewModel
import io.github.juevigrace.diva.core.Option
import io.github.juevigrace.diva.ui.components.layout.Screen
import io.github.juevigrace.diva.ui.components.layout.bars.top.TopNavBar
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerificationScreen(
    action: VerificationAction,
    viewModel: VerificationViewModel = koinViewModel(parameters = { parametersOf(action) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Screen(
        topBar = {
            TopNavBar(
                title = {
                    Text(text = stringResource(Res.string.verification_code))
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(VerificationEvents.OnBack) }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_chevron_left),
                            contentDescription = stringResource(Res.string.go_back)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.verification_code),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = stringResource(Res.string.verification_code_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = state.verificationForm.token,
                onValueChange = { viewModel.onEvent(VerificationEvents.OnTokenChanged(it)) },
                label = { Text(stringResource(Res.string.verification_code)) },
                placeholder = {
                    Text(
                        text = stringResource(Res.string.verification_code_placeholder),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                },
                isError = state.formValidation.tokenError is Option.Some,
                supportingText = when (val error = state.formValidation.tokenError) {
                    is Option.Some -> { { Text(stringResource(error.value)) } }
                    is Option.None -> null
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        viewModel.onEvent(VerificationEvents.OnSubmit)
                    }
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.onEvent(VerificationEvents.OnSubmit) },
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
                    Text(stringResource(Res.string.verify))
                }
            }
        }
    }
}
