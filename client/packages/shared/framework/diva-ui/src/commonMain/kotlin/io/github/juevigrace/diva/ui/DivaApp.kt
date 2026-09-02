package io.github.juevigrace.diva.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import io.github.juevigrace.diva.ui.dialog.DialogController
import io.github.juevigrace.diva.ui.dialog.LocalDialogController
import io.github.juevigrace.diva.ui.theme.DivaTheme
import io.github.juevigrace.diva.ui.theme.DivaThemeConfig
import io.github.juevigrace.diva.ui.toast.LocalToaster
import io.github.juevigrace.diva.ui.toast.Toaster

@Composable
fun DivaApp(
    themeConfig: DivaThemeConfig = DivaThemeConfig(),
    toaster: Toaster = Toaster.create(),
    dialogController: DialogController = DialogController.create(),
    content: @Composable () -> Unit,
) {
    DivaApp(
        theme = { content ->
            DivaTheme(config = themeConfig, content = content)
        },
        toaster = toaster,
        dialogController = dialogController,
        content = content
    )
}

@Composable
fun DivaApp(
    theme: @Composable (content: @Composable () -> Unit) -> Unit = { content ->
        DivaTheme(content = content)
    },
    toaster: Toaster = Toaster.create(),
    dialogController: DialogController = DialogController.create(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalToaster provides toaster,
        LocalDialogController provides dialogController,
    ) {
        theme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                content = content,
            )
        }
    }
}
