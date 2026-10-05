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
import androidx.compose.material3.TopAppBar
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
import com.diva.app.folder.presentation.ui.components.FoldersContent
import com.diva.app.folder.presentation.ui.components.navigation.FolderRoute
import com.diva.app.folder.presentation.ui.components.navigation.FoldersRoute
import com.diva.app.folder.presentation.viewmodel.FolderViewModel
import com.diva.app.home.presentation.ui.components.HomeContent
import com.diva.app.home.presentation.ui.components.navigation.HomeRoute
import com.diva.app.home.presentation.viewmodel.HomeViewModel
import com.diva.app.library.presentation.ui.components.LibraryContent
import com.diva.app.library.presentation.ui.components.navigation.LibraryRoute
import com.diva.app.library.presentation.viewmodel.LibraryViewModel
import com.diva.app.player.presentation.ui.components.MiniPlayer
import com.diva.app.player.presentation.viewmodel.PlayerViewModel
import com.diva.app.profile.presentation.ui.components.ProfileContent
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.profile.presentation.viewmodel.ProfileViewModel
import com.diva.app.search.presentation.ui.components.SearchResultsContent
import com.diva.app.search.presentation.ui.components.navigation.SearchResultsRoute
import com.diva.app.search.presentation.viewmodel.SearchViewModel
import io.github.juevigrace.diva.ui.layout.AdaptiveScreen
import io.github.juevigrace.diva.ui.layout.adaptiveNavigationStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle
import io.github.juevigrace.diva.ui.navigation.LocalTabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavHost
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Container for every tab. It owns the adaptive chrome: the bottom bar, the rail or
 * drawer, and a single top bar that swaps based on whichever destination is on top of
 * the active tab's stack.
 *
 * Tab content is state driven and free of [io.github.juevigrace.diva.ui.layout.Screen]
 * wrappers, so a tab can push sub destinations and still share one top bar.
 *
 * The search and folder view models are resolved here rather than inside their entries
 * on purpose. Nav3 gives every entry its own ViewModelStore, so a view model created in
 * the content would be a different instance from the one the top bar reads.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val playerViewModel: PlayerViewModel = koinViewModel()
    val playerState by playerViewModel.state.collectAsStateWithLifecycle()
    val libraryViewModel: LibraryViewModel = koinViewModel()
    val libraryState by libraryViewModel.state.collectAsStateWithLifecycle()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val profileState by profileViewModel.state.collectAsStateWithLifecycle()
    val searchViewModel: SearchViewModel = koinViewModel()
    val searchState by searchViewModel.state.collectAsStateWithLifecycle()
    val folderViewModel: FolderViewModel = koinViewModel()
    val folderState by folderViewModel.state.collectAsStateWithLifecycle()

    val tabNavigator: TabNavigator = LocalTabNavigator.current
    val tabBackStack by tabNavigator.tabBackStack.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Resolved once here rather than through LocalNavStyle: the top bar sits outside the
    // provider scope that AdaptiveScreen installs for the rail and drawers.
    val style = adaptiveNavigationStyle()

    AdaptiveScreen(
        style = style,
        bottomBar = {
            Column {
                MiniPlayer(
                    state = playerState,
                    onEvent = playerViewModel::onEvent,
                )
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
        },
        drawerState = drawerState,
        navContent = {
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
    ) { _ ->
        TabNavHost(
            modifier = Modifier.fillMaxSize(),
            tabNavigator = tabNavigator,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    HomeContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                    )
                }
                entry<SearchResultsRoute> {
                    SearchResultsContent(
                        state = searchState,
                        onEvent = searchViewModel::onEvent,
                    )
                }
                entry<FoldersRoute> {
                    FoldersContent(
                        state = folderState,
                        onEvent = folderViewModel::onEvent,
                    )
                }
                entry<FolderRoute> {
                    FoldersContent(
                        state = folderState,
                        onEvent = folderViewModel::onEvent,
                    )
                }
                entry<LibraryRoute> {
                    LibraryContent(
                        state = libraryState,
                        onEvent = libraryViewModel::onEvent,
                    )
                }
                entry<ProfileRoute> {
                    ProfileContent(
                        state = profileState,
                        onEvent = profileViewModel::onEvent,
                    )
                }
            }
        )
    }
}
