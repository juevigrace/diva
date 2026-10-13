package io.github.juevigrace.diva.ui.layout.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Hamburger button that opens the navigation drawer.
 *
 * The button is presentation only: it renders a menu icon and forwards the
 * tap to [onClick]. Showing or hiding it (for example only under
 * [io.github.juevigrace.diva.ui.layout.navigation.NavStyle.ModalDrawer]) is
 * up to the consumer.
 *
 * @param onClick invoked when the button is tapped; typically
 * `scope.launch { drawerState.open() }` on a hoisted [androidx.compose.material3.DrawerState].
 */
@Composable
fun DrawerIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = "Open navigation menu",
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Rounded.Menu,
            contentDescription = contentDescription,
        )
    }
}
