package com.diva.app.home.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
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
import com.diva.app.collection.presentation.ui.components.navigation.albumNav
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.app_name
import com.diva.app.home.presentation.events.HomeEvents
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.home.presentation.ui.components.DesktopSearchBar
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.components.HomeTopBar
import com.diva.app.home.presentation.ui.components.ProfileMenuDialog
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.ui.components.navigation.NavDirectionRow
import com.diva.app.home.presentation.ui.components.navigation.homeNav
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.library.presentation.ui.components.navigation.libraryNav
import com.diva.app.media.presentation.ui.components.navigation.mediaNav
import com.diva.app.mix.presentation.ui.components.navigation.mixNav
import com.diva.app.player.presentation.ui.components.MiniPlayer
import com.diva.app.player.presentation.viewmodel.PlayerViewModel
import com.diva.app.playlist.presentation.ui.components.navigation.playlistNav
import com.diva.app.profile.presentation.ui.components.navigation.profileNav
import com.diva.app.search.presentation.events.SearchEvents
import com.diva.app.search.presentation.ui.components.navigation.searchNav
import com.diva.app.search.presentation.viewmodel.SearchViewModel
import com.diva.app.server.presentation.ui.components.navigation.serverNav
import com.diva.app.settings.presentation.ui.components.navigation.settingsNav
import io.github.juevigrace.diva.lib.devices.presentation.ui.components.navigation.devicesNav
import io.github.juevigrace.diva.lib.file.presentation.ui.components.navigation.fileNav
import io.github.juevigrace.diva.lib.permissions.presentation.ui.components.navigation.permissionsNav
import io.github.juevigrace.diva.lib.session.presentation.ui.components.navigation.sessionNav
import io.github.juevigrace.diva.lib.user.presentation.ui.components.navigation.userNav
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

    // Hoisted so the desktop top-bar field and the search tab screen share one query.
    val searchViewModel: SearchViewModel = koinViewModel()
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()

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
        topBar = {
            if (state.settings.isDesktop) {
                HomeTopBar(
                    modifier = Modifier.padding(vertical = 4.dp),
                    navigationIcon = {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (windowInfo.widthSizeClass != WindowWidthSizeClass.Expanded) {
                                DrawerIconButton(onClick = { scope.launch { drawerState.open() } })
                            }
                            NavDirectionRow(
                                onBack = { viewModel.onEvent(HomeEvents.OnBack) },
                                onForward = { viewModel.onEvent(HomeEvents.OnForward) },
                                backEnabled = state.canGoBack,
                                forwardEnabled = state.canGoForward,
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                            )
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
                            DesktopSearchBar(
                                modifier = if (windowInfo.widthSizeClass == WindowWidthSizeClass.Compact) {
                                    Modifier.weight(1f)
                                } else {
                                    Modifier.width(400.dp)
                                },
                                query = searchState.query,
                                results = searchState.displayed,
                                isQueryEmpty = searchState.isQueryEmpty,
                                onQueryChange = {
                                    searchViewModel.onEvent(SearchEvents.OnQueryChange(it))
                                },
                                onClear = {
                                    searchViewModel.onEvent(SearchEvents.OnClear)
                                },
                                onOpenResults = {
                                    viewModel.onEvent(HomeEvents.OnOpenSearchResults)
                                },
                            )
                        }
                    },
                    onEvent = viewModel::onEvent
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
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .fillMaxSize(),
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
                searchNav(searchViewModel)
                fileNav()
                // TODO: profile module folds into the user module; screen redesign pending
                profileNav()

                settingsNav()
                albumNav()
                mediaNav()
                mixNav()
                playlistNav()
                serverNav()
                sessionNav()
                userNav()
                devicesNav()
                permissionsNav()
            }
        )
    }

    if (state.showProfileDialog) {
        ProfileMenuDialog(
            displayName = state.displayName,
            username = state.user.username,
            otherAccounts = state.accounts.filter { it.id != state.user.id },
            accountsExpanded = state.accountsExpanded,
            selectedAccountId = state.selectedAccountId,
            onToggleAccounts = { viewModel.onEvent(HomeEvents.OnToggleAccounts) },
            onSelectAccount = { viewModel.onEvent(HomeEvents.OnSelectAccount(it)) },
            onAddAccount = { viewModel.onEvent(HomeEvents.OnAddAccount) },
            onOpenProfile = { viewModel.onEvent(HomeEvents.OnOpenProfile) },
            onOpenSettings = { viewModel.onEvent(HomeEvents.OnOpenSettings) },
            onSignOut = { viewModel.onEvent(HomeEvents.OnSignOut) },
            onDismiss = { viewModel.onEvent(HomeEvents.OnDismissProfile) },
        )
    }
}
