package com.diva.app

import android.app.Application
import com.diva.app.di.appModule
import com.diva.app.settings.models.AppSettings
import io.github.juevigrace.diva.core.Option
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DivaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DivaApp)
            modules(
                appModule(
                    AppSettings(
                        protocol = "http",
                        port = Option.of(8080),
                        host = "localhost",
                    )
                )
            )
        }
    }
}
