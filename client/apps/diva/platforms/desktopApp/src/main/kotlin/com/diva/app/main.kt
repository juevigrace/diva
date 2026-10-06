package com.diva.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.diva.app.di.appModule
import com.diva.app.presentation.ui.screen.App
import com.diva.app.settings.models.DivaSettings
import io.github.juevigrace.diva.core.Option
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(
            appModule(
                DivaSettings(
                    protocol = "http",
                    port = Option.of(8080),
                    host = "localhost",
                    isDesktop = true,
                )
            )
        )
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "Diva",
    ) {
        App()
    }
}
