package com.diva.app.ui.di

import com.diva.app.ui.navigation.HomeRoute
import io.github.juevigrace.diva.ui.dialog.DialogController
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.toast.Toaster
import org.koin.core.module.Module
import org.koin.dsl.module

fun uiModule(): Module {
    return module {
        single<DialogController> { DialogController.create() }
        single<Toaster> { Toaster.create() }
        single<Navigator> { Navigator.create(startDestination = HomeRoute) }
    }
}
