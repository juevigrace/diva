package com.diva.app.navigation.routes

import androidx.navigation3.runtime.NavKey
import io.github.juevigrace.diva.ui.dialog.DialogController
import io.github.juevigrace.diva.ui.navigation.Navigator
import io.github.juevigrace.diva.ui.toast.Toaster
import kotlinx.serialization.Serializable
import org.koin.core.module.Module
import org.koin.dsl.module

@Serializable
data object HomeRoute : NavKey

fun uiModule(): Module {
    return module {
        single<DialogController> { DialogController.create() }
        single<Toaster> { Toaster.create() }
        single<Navigator> { Navigator.create(startDestination = HomeRoute) }
    }
}
