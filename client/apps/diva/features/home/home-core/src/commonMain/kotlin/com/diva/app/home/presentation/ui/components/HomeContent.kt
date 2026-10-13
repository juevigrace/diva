package com.diva.app.home.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.diva.app.generated.resources.Res
import com.diva.app.generated.resources.app_name
import com.diva.app.generated.resources.home
import com.diva.app.generated.resources.puerro
import com.diva.app.home.presentation.events.HomeEvents
import com.diva.app.home.presentation.state.HomeState
import com.diva.app.profile.presentation.ui.components.navigation.ProfileRoute
import com.diva.app.ui.components.CarouselCard
import com.diva.app.ui.components.CarouselSection
import io.github.juevigrace.diva.lib.ui.components.buttons.NotificationsIconButton
import io.github.juevigrace.diva.lib.ui.components.buttons.ProfileIconButton
import io.github.juevigrace.diva.ui.layout.Screen
import io.github.juevigrace.diva.ui.navigation.LocalNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * The home tab root. Named `HomeContent` rather than `HomeScreen` because
 * [com.diva.app.home.presentation.ui.screen.HomeScreen] is the tab host.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeState,
    onEvent: (HomeEvents) -> Unit,
) {
    val root = LocalNavigator.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Screen(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (!state.settings.isDesktop) {
                CenterAlignedTopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(Res.drawable.puerro),
                                contentDescription = null
                            )
                            Text(
                                text = stringResource(Res.string.app_name),
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    actions = {
                        // TODO: navigate to notifications or make a dropbox
                        NotificationsIconButton(onClick = {})
                        ProfileIconButton(onClick = { root.navigate(ProfileRoute) })
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                // TODO: filter chips
            }

            if (state.showRecent) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = "Recent",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
                item {
                    CarouselSection(
                        subtitle = {
                            Text(
                                text = "Played collections",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        items = state.recentCollections,
                    ) { item, modifier ->
                        CarouselCard(
                            alt = item.name,
                            title = item.name,
                            modifier = modifier,
                        )
                    }
                }

                item {
                    CarouselSection(
                        subtitle = {
                            Text(
                                text = "Recent media",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        items = state.recentMedia,
                    ) { item, modifier ->
                        CarouselCard(
                            alt = item.title,
                            title = item.title,
                            modifier = modifier,
                        )
                    }
                }
            }

            if (state.showRecommended) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        Text(
                            text = "All For You",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }

                item {
                    CarouselSection(
                        subtitle = {
                            Text(
                                text = "Recommended collections, albums, playlists and more",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        items = state.recommendedCollections,
                    ) { item, modifier ->
                        CarouselCard(
                            alt = item.name,
                            title = item.name,
                            modifier = modifier,
                        )
                    }
                }
                item {
                    CarouselSection(
                        subtitle = {
                            Text(
                                text = "Recommended collections, albums, playlists and more",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        items = state.recommendedMedia,
                    ) { item, modifier ->
                        CarouselCard(
                            alt = item.title,
                            title = item.title,
                            modifier = modifier,
                        )
                    }
                }
            }
        }
    }
}
