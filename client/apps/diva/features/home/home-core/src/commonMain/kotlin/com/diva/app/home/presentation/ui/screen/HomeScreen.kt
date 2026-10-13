package com.diva.app.home.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.folder.presentation.ui.components.navigation.folderNav
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.app_name
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.ui.components.navigation.homeNav
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.library.presentation.ui.components.navigation.libraryNav
import com.diva.app.player.presentation.ui.components.MiniPlayer
import com.diva.app.player.presentation.viewmodel.PlayerViewModel
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.profile.presentation.ui.components.navigation.profileNav
import com.diva.app.search.presentation.ui.components.navigation.searchNav
import io.github.juevigrace.diva.lib.ui.components.buttons.NotificationsIconButton
import io.github.juevigrace.diva.lib.ui.components.buttons.ProfileIconButton
import io.github.juevigrace.diva.lib.ui.components.input.SearchField
import io.github.juevigrace.diva.ui.layout.AdaptiveScreen
import io.github.juevigrace.diva.ui.layout.adaptiveNavigationStyle
import io.github.juevigrace.diva.ui.layout.buttons.DrawerIconButton
import io.github.juevigrace.diva.ui.layout.navigation.LocalNavStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle
import io.github.juevigrace.diva.ui.layout.rememberNavSurfaceState
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavHost
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.window.rememberWindowInfo
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state: HomeState by viewModel.state.collectAsStateWithLifecycle()

    val playerViewModel: PlayerViewModel = koinViewModel()
    val playerState by playerViewModel.state.collectAsStateWithLifecycle()

    val tabNavigator: TabNavigator = LocalTabNavigator.current
    val tabBackStack by tabNavigator.tabBackStack.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val windowInfo = rememberWindowInfo()

    AdaptiveScreen(
        style = if (state.settings.isDesktop) {
            if (windowInfo.widthSizeClass == WindowWidthSizeClass.Expanded) {
                // TODO: might change the styling and remove this
                NavStyle.Rail
            } else {
                NavStyle.ModalDrawer
            }
        } else {
            adaptiveNavigationStyle()
        },
        contentWindowInsets = WindowInsets(0),
        topBar = {
            if (state.settings.isDesktop) {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(4.dp),
                    navigationIcon = {
                        if (windowInfo.widthSizeClass != WindowWidthSizeClass.Expanded) {
                            DrawerIconButton(onClick = { scope.launch { drawerState.open() } })
                        }
                    },
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (windowInfo.widthSizeClass == WindowWidthSizeClass.Expanded) {
                                IconButton(
                                    onClick = {
                                        tabNavigator.selectTab(HomeRoute)
                                        tabNavigator.clearTabHistory()
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(HomeRoute.icon),
                                        contentDescription = stringResource(HomeRoute.title),
                                        modifier = Modifier.size(24.dp),
                                    )
                                }
                            }

                            SearchField(
                                modifier = if (windowInfo.widthSizeClass == WindowWidthSizeClass.Compact) {
                                    Modifier.weight(1f)
                                } else {
                                    Modifier.width(400.dp)
                                },
                                value = "",
                                onEvent = { e -> }
                            )
                        }
                    },
                    actions = {
                        // TODO: navigate to notifications or make a dropbox
                        NotificationsIconButton(onClick = {})
                        ProfileIconButton(onClick = { tabNavigator.selectTab(ProfileRoute) })
                    },
                )
            }
        },
        bottomBar = {
            val style = LocalNavStyle.current
            Column {
                MiniPlayer(
                    state = playerState,
                    onEvent = playerViewModel::onEvent,
                )
                if (windowInfo.isPortrait && style == NavStyle.BottomBar) {
                    BottomAppBar {
                        tabNavigator.tabs.forEach { tab ->
                            NavigationBarItem(
                                modifier = Modifier.weight(1f),
                                selected = tabBackStack.selectedTab == tab,
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
                    }
                }
            }
        },
        drawerState = drawerState,
        navSurface = rememberNavSurfaceState(drawerShape = RoundedCornerShape(0.dp)),
        navContent = {
            val style = LocalNavStyle.current
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    when (style) {
                        NavStyle.ModalDrawer -> {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                contentAlignment = Alignment.CenterStart,
                            ) {
                                Text(
                                    text = stringResource(Res.string.app_name),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }

                            HorizontalDivider()
                        }

                        NavStyle.Rail -> {
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        else -> {}
                    }
                }
                items(tabNavigator.tabs) { tab ->
                    when (style) {
                        NavStyle.ModalDrawer, NavStyle.PermanentDrawer -> {
                            NavigationDrawerItem(
                                modifier = Modifier.padding(horizontal = 8.dp),
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
                homeNav {
                    HomeContent(state = state, onEvent = viewModel::onEvent)
                }
                libraryNav()
                // todo: make this show as a tab in mobile
                searchNav()
                folderNav()
                // TODO: move profile out here and replace with more tab
                profileNav()
            }
        )
    }
}
