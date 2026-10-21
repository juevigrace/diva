package io.github.juevigrace.diva.core.permissions.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity

internal inline fun createActivityLifecycleObserver(
    crossinline onCreated: (ComponentActivity) -> Unit = {},
    crossinline onDestroyed: (ComponentActivity) -> Unit = {},
): Application.ActivityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is ComponentActivity) {
            onCreated(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) {
        if (activity is ComponentActivity) {
            onDestroyed(activity)
        }
    }
}
