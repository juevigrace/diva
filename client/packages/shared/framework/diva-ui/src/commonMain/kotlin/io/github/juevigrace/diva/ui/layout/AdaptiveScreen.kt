package io.github.juevigrace.diva.ui.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.juevigrace.diva.ui.toast.ToasterHost
import io.github.juevigrace.diva.ui.window.rememberWindowInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveScreen(
    modifier: Modifier = Modifier,
    style: NavigationStyle = adaptiveNavigationStyle(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navContent: @Composable ColumnScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    snackBarHost: @Composable () -> Unit = { ToasterHost() },
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    when (style) {
        NavigationStyle.BottomBar -> Screen(
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackBarHost = snackBarHost,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content,
        )
        NavigationStyle.ModalDrawer -> ModalDrawerScreen(
            modifier = modifier,
            topBar = topBar,
            drawerState = drawerState,
            navContent = navContent,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackBarHost = snackBarHost,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content,
        )
        NavigationStyle.PermanentDrawer -> PermanentDrawerScreen(
            modifier = modifier,
            topBar = topBar,
            navContent = navContent,
            bottomBar = bottomBar,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackBarHost = snackBarHost,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content,
        )
        NavigationStyle.Rail -> RailScreen(
            modifier = modifier,
            topBar = topBar,
            navContent = navContent,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackBarHost = snackBarHost,
            containerColor = containerColor,
            contentColor = contentColor,
            contentWindowInsets = contentWindowInsets,
            content = content,
        )
    }
}

@Composable
fun adaptiveNavigationStyle(): NavigationStyle {
    val windowInfo = rememberWindowInfo()
    return if (windowInfo.widthSizeClass == WindowWidthSizeClass.Expanded) {
        NavigationStyle.Rail
    } else {
        NavigationStyle.BottomBar
    }
}