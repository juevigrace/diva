package com.diva.app.ui.di

import io.github.juevigrace.diva.ui.dialog.DialogController
import io.github.juevigrace.diva.ui.toast.Toaster
import org.koin.core.module.Module
import org.koin.dsl.module

fun uiModule(): Module {
    return module {
        single<DialogController> { DialogController.create() }
        single<Toaster> { Toaster.create() }
    }
}