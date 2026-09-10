package com.diva.app.features.home.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.features.home.presentation.viewmodel.HomeViewModel
import com.diva.app.features.library.presentation.ui.screen.LibraryScreen
import com.diva.app.features.profile.presentation.ui.screen.ProfileScreen
import com.diva.app.features.search.presentation.ui.screen.SearchScreen
import com.diva.app.ui.navigation.HomeRoute
import com.diva.app.ui.navigation.LibraryRoute
import com.diva.app.ui.navigation.ProfileRoute
import com.diva.app.ui.navigation.SearchRoute
import io.github.juevigrace.diva.ui.layout.AdaptiveScreen
import io.github.juevigrace.diva.ui.layout.bars.NavItem
import io.github.juevigrace.diva.ui.layout.bars.TabBar
import io.github.juevigrace.diva.ui.navigation.Tab
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavHost
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    tabNavigator: TabNavigator = koinInject(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
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
            TabBar(
                tabs = tabNavigator.tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> tabNavigator.selectTab(tabNavigator.tabs[index]) },
            )
        },
        drawerState = drawerState,
        navContent = {
            tabNavigator.tabs.forEachIndexed { index, tab ->
                NavItem(
                    selected = selectedTabIndex == index,
                    icon = tab.icon,
                    label = tab.title,
                    onClick = {
                        tabNavigator.selectTab(tab)
                        scope.launch { drawerState.close() }
                    },
                )
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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = state.value.title)
                    }
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