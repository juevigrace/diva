package io.github.juevigrace.diva.ui.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import io.github.juevigrace.diva.ui.layout.navigation.LocalNavStyle
import io.github.juevigrace.diva.ui.layout.navigation.NavStyle
import io.github.juevigrace.diva.ui.window.rememberWindowInfo

@Composable
fun AdaptiveScreen(
    modifier: Modifier = Modifier,
    style: NavStyle = adaptiveNavigationStyle(),
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navSurface: NavSurfaceState = rememberNavSurfaceState(),
    navContent: @Composable ColumnScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    snackBarHost: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    // Provided around the whole component, not just the nav surface, so topBar, bottomBar and
    // content report the resolved style and not a hardcoded BottomBar.
    CompositionLocalProvider(LocalNavStyle provides style) {
        when (style) {
            NavStyle.BottomBar -> Screen(
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
            NavStyle.ModalDrawer -> {
                val drawerContainer = navSurface.drawerContainerColor ?: DrawerDefaults.modalContainerColor
                ModalDrawerScreen(
                    modifier = modifier,
                    topBar = topBar,
                    drawerState = drawerState,
                    navContent = navContent,
                    gesturesEnabled = navSurface.gesturesEnabled,
                    scrimColor = navSurface.scrimColor ?: DrawerDefaults.scrimColor,
                    drawerShape = navSurface.drawerShape ?: DrawerDefaults.shape,
                    drawerContainerColor = drawerContainer,
                    drawerContentColor = navSurface.drawerContentColor ?: contentColorFor(drawerContainer),
                    drawerTonalElevation = navSurface.drawerTonalElevation ?: DrawerDefaults.ModalDrawerElevation,
                    drawerWindowInsets = navSurface.drawerWindowInsets ?: DrawerDefaults.windowInsets,
                    bottomBar = bottomBar,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    snackBarHost = snackBarHost,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    contentWindowInsets = contentWindowInsets,
                    content = content,
                )
            }
            NavStyle.PermanentDrawer -> {
                val drawerContainer = navSurface.drawerContainerColor ?: DrawerDefaults.standardContainerColor
                PermanentDrawerScreen(
                    modifier = modifier,
                    topBar = topBar,
                    navContent = navContent,
                    drawerShape = navSurface.drawerShape ?: RectangleShape,
                    drawerContainerColor = drawerContainer,
                    drawerContentColor = navSurface.drawerContentColor ?: contentColorFor(drawerContainer),
                    drawerTonalElevation = navSurface.drawerTonalElevation ?: DrawerDefaults.PermanentDrawerElevation,
                    drawerWindowInsets = navSurface.drawerWindowInsets ?: DrawerDefaults.windowInsets,
                    bottomBar = bottomBar,
                    floatingActionButton = floatingActionButton,
                    floatingActionButtonPosition = floatingActionButtonPosition,
                    snackBarHost = snackBarHost,
                    containerColor = containerColor,
                    contentColor = contentColor,
                    contentWindowInsets = contentWindowInsets,
                    content = content,
                )
            }
            NavStyle.Rail -> {
                val railContainer = navSurface.railContainerColor ?: NavigationRailDefaults.ContainerColor
                RailScreen(
                    modifier = modifier,
                    topBar = topBar,
                    navContent = navContent,
                    bottomBar = bottomBar,
                    railContainerColor = railContainer,
                    railContentColor = navSurface.railContentColor ?: contentColorFor(railContainer),
                    railHeader = navSurface.railHeader,
                    railWindowInsets = navSurface.railWindowInsets ?: NavigationRailDefaults.windowInsets,
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
    }
}

@Composable
fun adaptiveNavigationStyle(): NavStyle {
    val windowInfo = rememberWindowInfo()
    return if (windowInfo.widthSizeClass == WindowWidthSizeClass.Medium) {
        NavStyle.Rail
    } else {
        NavStyle.BottomBar
    }
}
