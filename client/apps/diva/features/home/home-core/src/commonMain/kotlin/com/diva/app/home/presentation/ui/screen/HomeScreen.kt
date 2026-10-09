package com.diva.app.home.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.folder.presentation.ui.components.navigation.folderNav
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.ui.components.navigation.bars.HomeNavContent
import com.diva.app.home.presentation.ui.components.navigation.homeNav
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.library.presentation.ui.components.navigation.libraryNav
import com.diva.app.player.presentation.ui.components.MiniPlayer
import com.diva.app.player.presentation.viewmodel.PlayerViewModel
import com.diva.app.profile.presentation.ui.components.navigation.profileNav
import com.diva.app.search.presentation.ui.components.SearchField
import com.diva.app.search.presentation.ui.components.navigation.searchNav
import com.diva.app.search.presentation.viewmodel.SearchViewModel
import io.github.juevigrace.diva.ui.layout.AdaptiveScreen
import io.github.juevigrace.diva.ui.layout.adaptiveNavigationStyle
import io.github.juevigrace.diva.ui.layout.navigation.LocalNavStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavHost
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.window.rememberWindowInfo
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
    val searchViewModel: SearchViewModel = koinViewModel()
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()

    val tabNavigator: TabNavigator = LocalTabNavigator.current
    val tabBackStack by tabNavigator.tabBackStack.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AdaptiveScreen(
        style = if (state.settings.isDesktop) NavStyle.Rail else adaptiveNavigationStyle(),
        contentWindowInsets = WindowInsets(0),
        topBar = {
            if (state.settings.isDesktop) {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
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

                            SearchField(
                                modifier = Modifier.width(300.dp),
                                state = searchState,
                                onEvent = searchViewModel::onEvent,
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            val style = LocalNavStyle.current
            val windowInfo = rememberWindowInfo()
            if (windowInfo.isPortrait) {
                Column {
                    MiniPlayer(
                        state = playerState,
                        onEvent = playerViewModel::onEvent,
                    )
                    if (style == NavStyle.BottomBar) {
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
            }
        },
        drawerState = drawerState,
        navContent = {
            HomeNavContent(drawerState = drawerState)
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
                searchNav(searchViewModel)
                folderNav()
                // TODO: move profile out here and replace with more tab
                profileNav()
            }
        )
    }
}
