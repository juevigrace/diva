package com.diva.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.diva.app.presentation.ui.screen.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Diva",
    ) {
        App()
    }
}
