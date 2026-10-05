package com.diva.app.search.presentation.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.search
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.state.SearchState
import org.jetbrains.compose.resources.stringResource

/**
 * The query field for the home tab top bar.
 *
 * [onBack] is only supplied when the field is rendered above a pushed search
 * results destination, in which case it becomes a back affordance instead of a
 * plain field. [requestFocus] is used by compact layouts, where search is opened
 * from an icon and the field should take the keyboard immediately.
 */
@Composable
fun SearchField(
    state: SearchState,
    onEvent: (SearchEvents) -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
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

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }

        OutlinedTextField(
            value = state.query,
            onValueChange = { onEvent(SearchEvents.OnQueryChange(it)) },
            placeholder = { Text(text = stringResource(Res.string.search)) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (!state.isQueryEmpty) {
                    IconButton(onClick = { onEvent(SearchEvents.OnClear) }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear")
                    }
                }
            },
            supportingText = {
                val total = state.totalResults
                if (total > 0) {
                    Text(
                        text = if (total == 1) "1 result" else "$total results",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .padding(end = if (onBack == null) 16.dp else 8.dp),
        )
    }
}
