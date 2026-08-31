package com.diva.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.juevigrace.diva.lib.shared.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Diva",
    ) {
        App()
    }
}
