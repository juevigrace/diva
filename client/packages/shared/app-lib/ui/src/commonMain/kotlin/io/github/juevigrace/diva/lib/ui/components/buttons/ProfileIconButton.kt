package io.github.juevigrace.diva.lib.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.juevigrace.diva.lib.generated.resources.Res
import io.github.juevigrace.diva.lib.generated.resources.profile
import org.jetbrains.compose.resources.stringResource

/**
 * Icon button that navigates to or opens the user profile.
 *
 * [image] is a loader-agnostic slot for the user's avatar: supply it backed by
 * whatever source you have (a local painter, a network image loader, ...). When
 * [image] is null a default person icon is rendered instead. A caller-supplied
 * [image] owns its own semantics and tint.
 *
 * @param onClick invoked when the button is tapped.
 * @param image Optional slot drawing the user's profile image.
 */
@Composable
fun ProfileIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    image: (@Composable () -> Unit)? = null,
    contentDescription: String = stringResource(Res.string.profile),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        if (image != null) {
            image()
        } else {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = contentDescription,
            )
        }
    }
}
