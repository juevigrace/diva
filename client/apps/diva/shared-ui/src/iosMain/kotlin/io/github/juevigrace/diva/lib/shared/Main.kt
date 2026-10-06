package io.github.juevigrace.diva.lib.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.diva.app.di.appModule
import com.diva.app.presentation.ui.screen.App
import com.diva.app.settings.models.DivaSettings
import io.github.juevigrace.diva.core.Option
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin {
        modules(
            appModule(
                DivaSettings(
                    protocol = "http",
                    port = Option.of(8080),
                    host = "localhost",
                )
            )
        )
    }

    return ComposeUIViewController { App() }
}
