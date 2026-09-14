package io.github.juevigrace.diva.ui.layout.navigation

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NavItem(
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: NavItemStyle = LocalNavItemStyle.current,
) {
    when (style) {
        NavItemStyle.Rail -> NavigationRailItem(
            modifier = modifier,
            selected = selected,
            onClick = onClick,
            icon = icon,
            label = {
                Text(text = stringResource(label))
            },
        )

        NavItemStyle.Drawer -> NavigationDrawerItem(
            modifier = modifier,
            label = {
                Text(text = stringResource(label))
            },
            selected = selected,
            onClick = onClick,
            icon = icon,
        )
    }
}
