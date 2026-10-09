package com.diva.app.home.presentation.ui.components.navigation.bars

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.juevigrace.diva.ui.layout.navigation.LocalNavStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ColumnScope.HomeNavContent(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    val tabNavigator: TabNavigator = LocalTabNavigator.current
    val tabBackStack by tabNavigator.tabBackStack.collectAsStateWithLifecycle()

    val style = LocalNavStyle.current
    tabNavigator.tabs.forEach { tab ->
        when (style) {
            NavStyle.ModalDrawer, NavStyle.PermanentDrawer -> {
                NavigationDrawerItem(
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(tab.title)) },
                    selected = tabBackStack.selectedTab == tab,
                    onClick = {
                        tabNavigator.selectTab(tab)
                        if (style == NavStyle.ModalDrawer) {
                            scope.launch { drawerState.close() }
                        }
                    },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(tab.icon),
                            contentDescription = null,
                        )
                    },
                )
            }
            NavStyle.Rail -> {
                NavigationRailItem(
                    selected = tabBackStack.selectedTab == tab,
                    onClick = { tabNavigator.selectTab(tab) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(tab.icon),
                            contentDescription = null,
                        )
                    },
                    label = { Text(stringResource(tab.title)) },
                )
            }
            NavStyle.BottomBar -> {}
        }
    }
}
