package io.github.juevigrace.diva.lib.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.juevigrace.diva.lib.generated.resources.Res
import io.github.juevigrace.diva.lib.generated.resources.notifications
import org.jetbrains.compose.resources.stringResource

/**
 * Icon button that opens the notifications surface, with an optional unread
 * indicator.
 *
 * When [badgeCount] is greater than zero the badge shows that number;
 * otherwise a dot is shown while [hasUnread] is true. With both at their
 * defaults the button renders no badge at all.
 *
 * @param onClick invoked when the button is tapped.
 * @param hasUnread whether to show an unread dot when [badgeCount] is zero.
 * @param badgeCount unread notification count shown in the badge; `0` hides it.
 */
@Composable
fun NotificationsIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    hasUnread: Boolean = false,
    badgeCount: Int = 0,
    contentDescription: String = stringResource(Res.string.notifications),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        BadgedBox(
            badge = {
                when {
                    badgeCount > 0 -> Badge { Text(badgeCount.toString()) }
                    hasUnread -> Badge()
                }
            },
        ) {
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = contentDescription,
            )
        }
    }
}
