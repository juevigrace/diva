package io.github.juevigrace.diva.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.juevigrace.diva.ui.layout.navigation.LocalNavStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle

@Composable
fun RailScreen(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    navContent: @Composable ColumnScope.() -> Unit,
    railContainerColor: Color = NavigationRailDefaults.ContainerColor,
    railContentColor: Color = contentColorFor(railContainerColor),
    railHeader: (@Composable ColumnScope.() -> Unit)? = null,
    railWindowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    snackBarHost: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    CompositionLocalProvider(LocalNavStyle provides NavStyle.Rail) {
        Screen(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackBarHost = snackBarHost,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                NavigationRail(
                    modifier = Modifier,
                    containerColor = railContainerColor,
                    contentColor = railContentColor,
                    header = railHeader,
                    windowInsets = railWindowInsets,
                    content = navContent,
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                ) {
                    // The Row above already consumed innerPadding, so content must not
                    // apply it again or every slot gets double-inset.
                    content(PaddingValues(0.dp))
                }
            }
        }
    }
}
