package io.github.juevigrace.diva.lib.ui.components.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import io.github.juevigrace.diva.lib.generated.resources.Res
import io.github.juevigrace.diva.lib.generated.resources.search
import org.jetbrains.compose.resources.stringResource

/**
 * All-purpose search field with no feature state of its own.
 *
 * Text lives in [value]; every interaction leaves through [onEvent]
 * ([SearchFieldEvents.OnQueryChange], [SearchFieldEvents.OnSearch],
 * [SearchFieldEvents.OnClear]). Defaults reproduce the standard search
 * styling: search leading icon, clear trailing icon while the query is
 * non-empty, a "Search" placeholder, and the search IME action. The
 * [keyboardActions] passed in still run for their own actions; a custom
 * `onSearch` runs after this field's built-in hide-keyboard plus event
 * dispatch.
 *
 * [requestFocus] is used by compact layouts, where search is opened from an
 * icon and the field should take the keyboard immediately.
 */
@Composable
fun SearchField(
    value: String,
    onEvent: (SearchFieldEvents) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = {
        Text(text = stringResource(Res.string.search))
    },
    supportingText: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(Icons.Filled.Search, contentDescription = null)
    },
    trailingIcon: @Composable (() -> Unit)? = {
        if (value.isNotEmpty()) {
            IconButton(onClick = { onEvent(SearchFieldEvents.OnClear) }) {
                Icon(Icons.Filled.Close, contentDescription = "Clear")
            }
        }
    },
    isError: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    singleLine: Boolean = true,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions(),
    interactionSource: MutableInteractionSource? = null,
    requestFocus: Boolean = false,
) {
    val focusRequester = FocusRequester()
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(requestFocus) {
        if (requestFocus) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }

    val mergedKeyboardActions = KeyboardActions(
        onSearch = {
            keyboard?.hide()
            onEvent(SearchFieldEvents.OnSearch)
            keyboardActions.onSearch?.invoke(this)
        },
        onDone = keyboardActions.onDone,
        onGo = keyboardActions.onGo,
        onNext = keyboardActions.onNext,
        onPrevious = keyboardActions.onPrevious,
        onSend = keyboardActions.onSend,
    )

    OutlinedTextField(
        value = value,
        onValueChange = { onEvent(SearchFieldEvents.OnQueryChange(it)) },
        modifier = modifier.focusRequester(focusRequester),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        supportingText = supportingText,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = mergedKeyboardActions,
        singleLine = singleLine,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}
