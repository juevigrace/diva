package io.github.juevigrace.diva.ui.layout.bars

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class NavItemStyle {
    Rail,
    Drawer,
}

val LocalNavItemStyle = staticCompositionLocalOf { NavItemStyle.Drawer }

@Composable
fun ColumnScope.NavItem(
    selected: Boolean,
    icon: DrawableResource,
    label: StringResource,
    onClick: () -> Unit,
) {
    when (LocalNavItemStyle.current) {
        NavItemStyle.Rail -> NavigationRailItem(
            selected = selected,
            onClick = onClick,
            icon = {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(label),
                )
            },
            label = {
                Text(text = stringResource(label))
            },
        )

        NavItemStyle.Drawer -> NavigationDrawerItem(
            label = {
                Text(text = stringResource(label))
            },
            selected = selected,
            onClick = onClick,
            icon = {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(label),
                )
            },
        )
    }
}