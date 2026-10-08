package io.github.juevigrace.diva.lib.ui.components.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import io.github.juevigrace.diva.lib.generated.resources.Res
import io.github.juevigrace.diva.lib.generated.resources.hide_text
import io.github.juevigrace.diva.lib.generated.resources.ic_eye
import io.github.juevigrace.diva.lib.generated.resources.ic_eye_off
import io.github.juevigrace.diva.lib.generated.resources.show_text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Password text field with an eye toggle that shows or hides the input.
 *
 * The toggle lives in the trailing icon slot, so [OutlinedTextField]'s own
 * trailing icon is not part of the signature. [visibilityToggle] receives the
 * current visibility and defaults to the shared eye drawables with localized
 * content descriptions; pass your own lambda to restyle or replace it. The
 * rest of the signature is the standard outlined field surface.
 */
@Composable
fun SecureTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    visibilityToggle: @Composable (isVisible: Boolean) -> Unit = { isVisible ->
        Icon(
            modifier = Modifier.size(24.dp),
            painter = painterResource(
                if (isVisible) Res.drawable.ic_eye_off else Res.drawable.ic_eye
            ),
            contentDescription = stringResource(
                if (isVisible) Res.string.hide_text else Res.string.show_text
            ),
        )
    },
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    singleLine: Boolean = true,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource? = null,
) {
    var showText by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = {
            IconButton(onClick = { showText = !showText }) {
                visibilityToggle(showText)
            }
        },
        supportingText = supportingText,
        isError = isError,
        singleLine = singleLine,
        visualTransformation = if (showText) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}
