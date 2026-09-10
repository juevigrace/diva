package com.diva.app.features.home.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import com.diva.app.features.home.presentation.viewmodel.HomeViewModel
import com.diva.app.ui.navigation.HomeRoute
import com.diva.app.ui.navigation.LibraryRoute
import com.diva.app.ui.navigation.ProfileRoute
import com.diva.app.ui.navigation.SearchRoute
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.layout.bars.TabBar
import io.github.juevigrace.diva.ui.navigation.Tab
import io.github.juevigrace.diva.ui.navigation.TabNavigator
import io.github.juevigrace.diva.ui.navigation.TabNavHost
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
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

    Screen(
        bottomBar = {
            TabBar(
                tabs = tabNavigator.tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> tabNavigator.selectTab(tabNavigator.tabs[index]) },
            )
        }
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
                    TabPlaceholder(title = SearchRoute.title)
                }
                entry<LibraryRoute> {
                    TabPlaceholder(title = LibraryRoute.title)
                }
                entry<ProfileRoute> {
                    TabPlaceholder(title = ProfileRoute.title)
                }
            }
        )
    }
}

@Composable
private fun TabPlaceholder(title: StringResource) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = stringResource(title))
    }
}