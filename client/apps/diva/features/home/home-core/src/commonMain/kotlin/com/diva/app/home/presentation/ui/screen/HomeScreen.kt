package com.diva.app.home.presentation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.folder.presentation.ui.components.FolderDetailScreen
import com.diva.app.folder.presentation.ui.components.FoldersScreen
import com.diva.app.folder.presentation.ui.components.navigation.FolderRoute
import com.diva.app.folder.presentation.ui.components.navigation.FoldersRoute
import com.diva.app.folder.presentation.viewmodel.FolderViewModel
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.library.presentation.ui.components.LibraryScreen
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.player.presentation.ui.components.MiniPlayer
import com.diva.app.player.presentation.viewmodel.PlayerViewModel
import com.diva.app.profile.presentation.ui.components.ProfileScreen
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.search.presentation.ui.components.SearchResultsScreen
import com.diva.app.search.presentation.ui.components.navigation.SearchResultsRoute
import com.diva.app.search.presentation.viewmodel.SearchViewModel
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

/**
 * Container for every tab. It owns the adaptive chrome only: the bottom bar and the rail
 * or drawer. Every destination below it is a self contained screen that brings its own
 * [io.github.juevigrace.diva.ui.layout.Screen] and top bar, so there is nothing left to
 * swap out centrally.
 *
 * Three view models are resolved here rather than inside their entries on purpose. Nav3
 * gives every entry its own ViewModelStore, so a view model created inside an entry is a
 * different instance from the one another entry reads:
 *  - the player, because its mini player sits in the bottom bar, outside the nav host;
 *  - search, shared by the home tab's inline field and the search results destination;
 *  - folders, shared by the tab root and the pushed directory detail.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val playerViewModel: PlayerViewModel = koinViewModel()
    val playerState by playerViewModel.state.collectAsStateWithLifecycle()
    val searchViewModel: SearchViewModel = koinViewModel()
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()
    val folderViewModel: FolderViewModel = koinViewModel()
    val folderState by folderViewModel.state.collectAsStateWithLifecycle()

    val tabNavigator: TabNavigator = LocalTabNavigator.current
    val tabBackStack by tabNavigator.tabBackStack.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    AdaptiveScreen(
        bottomBar = {
            val style = LocalNavStyle.current
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
                                alwaysShowLabel = true,
                            )
                        }
                    }
                }
            }
        },
        drawerState = drawerState,
        navContent = {
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
                                    contentDescription = stringResource(tab.title),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            tabNavigator = tabNavigator,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    HomeContent(
                        searchState = searchState,
                        onSearchEvent = searchViewModel::onEvent,
                    )
                }
                entry<SearchResultsRoute> {
                    SearchResultsScreen(
                        state = searchState,
                        onEvent = searchViewModel::onEvent,
                        onBack = { tabNavigator.pop() },
                    )
                }
                entry<FoldersRoute> {
                    FoldersScreen(
                        state = folderState,
                        onEvent = folderViewModel::onEvent,
                    )
                }
                entry<FolderRoute> { key ->
                    folderViewModel.onEnter(key.folderId)
                    FolderDetailScreen(
                        state = folderState,
                        onEvent = folderViewModel::onEvent,
                        onBack = { tabNavigator.pop() },
                    )
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
