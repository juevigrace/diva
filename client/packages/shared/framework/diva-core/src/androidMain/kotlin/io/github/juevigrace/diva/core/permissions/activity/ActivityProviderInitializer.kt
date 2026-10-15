package io.github.juevigrace.diva.core.permissions.activity

import android.content.Context
import androidx.startup.Initializer

internal class ActivityProviderInitializer : Initializer<ActivityProvider> {

    override fun create(context: Context): ActivityProvider = ActivityProvider.create(context)

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
