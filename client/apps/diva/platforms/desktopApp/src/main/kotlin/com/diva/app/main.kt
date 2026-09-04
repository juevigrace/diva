package com.diva.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.diva.app.di.appModule
import com.diva.app.presentation.ui.screen.App
import org.koin.core.context.startKoin

fun main() = application {
    startKoin { modules(appModule()) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Diva",
    ) {
        App()
    }
}
