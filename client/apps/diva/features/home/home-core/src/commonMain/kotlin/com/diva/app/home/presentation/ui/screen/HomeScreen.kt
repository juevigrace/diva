package com.diva.app.home.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.library.presentation.ui.screen.LibraryScreen
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.profile.presentation.ui.screen.ProfileScreen
import com.diva.app.search.presentation.ui.components.navigation.SearchRoute
import com.diva.app.search.presentation.ui.screen.SearchScreen
import io.github.juevigrace.diva.ui.layout.AdaptiveScreen
import io.github.juevigrace.diva.ui.layout.navigation.LocalNavStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavHost
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabNavigator: TabNavigator = LocalTabNavigator.current
    val backStack by tabNavigator.backStack.collectAsStateWithLifecycle()

    val selectedTabIndex = remember(backStack.selectedTab) {
        tabNavigator.tabs
            .indexOfFirst { it.route == backStack.selectedTab }
            .coerceAtLeast(0)
    }

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AdaptiveScreen(
        bottomBar = {
            BottomAppBar {
                tabNavigator.tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        modifier = Modifier.weight(1f),
                        selected = selectedTabIndex == index,
                        onClick = { tabNavigator.selectTab(tab) },
                        icon = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(tab.icon),
                                contentDescription = stringResource(tab.title),
                            )
                        },
                        label = { Text(stringResource(tab.title)) },
                        alwaysShowLabel = true,
                    )
                }
            }
        },
        drawerState = drawerState,
        navContent = {
            val style = LocalNavStyle.current
            tabNavigator.tabs.forEachIndexed { index, tab ->
                when (style) {
                    NavStyle.ModalDrawer, NavStyle.PermanentDrawer -> {
                        NavigationDrawerItem(
                            modifier = Modifier.weight(1f),
                            label = { Text(stringResource(tab.title)) },
                            selected = selectedTabIndex == index,
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
                                    contentDescription = stringResource(tab.title),
                                )
                            },
                        )
                    }
                    NavStyle.Rail -> {
                        NavigationRailItem(
                            selected = selectedTabIndex == index,
                            onClick = { tabNavigator.selectTab(tab) },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(tab.icon),
                                    contentDescription = stringResource(tab.title),
                                )
                            },
                            label = { Text(stringResource(tab.title)) },
                        )
                    }
                    NavStyle.BottomBar -> {}
                }
            }
        },
    ) { innerPadding ->
        TabNavHost(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            tabNavigator = tabNavigator,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    HomeContent(
                        state = state,
                    )
                }
                entry<SearchRoute> {
                    SearchScreen()
                }
                entry<LibraryRoute> {
                    LibraryScreen()
                }
                entry<ProfileRoute> {
                    ProfileScreen()
                }
            }
        )
    }
}
