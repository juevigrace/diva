package io.github.juevigrace.diva.lib.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.diva.app.di.appModule
import com.diva.app.presentation.ui.screen.App
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController {
    startKoin { modules(appModule()) }

    App()
}
