package io.github.juevigrace.diva.core.permissions.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import java.lang.ref.WeakReference

internal class ActivityProvider(context: Context) {

    val appContext: Context = context.applicationContext ?: context

    private var activityRef: WeakReference<ComponentActivity>? = null
    private var launcherRef: WeakReference<ActivityResultLauncher<Array<String>>>? = null
    private var activityLauncherRef: WeakReference<ActivityResultLauncher<Intent>>? = null
    private var onResult: ((Map<String, Boolean>) -> Unit)? = null
    private var onActivityResult: (() -> Unit)? = null

    val activity: ComponentActivity
        get() = activityRef?.get() ?: error("No ComponentActivity is available")

    private val lifecycleObserver = createActivityLifecycleObserver { activity ->
        activityRef = WeakReference(activity)
        val launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { results ->
            val callback = onResult
            onResult = null
            callback?.invoke(results)
        }
        launcherRef = WeakReference(launcher)

        val activityLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            val callback = onActivityResult
            onActivityResult = null
            callback?.invoke()
        }
        activityLauncherRef = WeakReference(activityLauncher)
    }

    init {
        val application = appContext as? Application
        application?.registerActivityLifecycleCallbacks(lifecycleObserver)
            ?: run { activityRef = WeakReference(appContext as? ComponentActivity) }
    }

    fun launch(permissions: List<String>, callback: (Map<String, Boolean>) -> Unit) {
        val launcher = launcherRef?.get() ?: error("No ActivityResultLauncher is available")
        onResult = callback
        launcher.launch(permissions.toTypedArray())
    }

    fun launchActivity(intent: Intent, callback: () -> Unit) {
        val launcher = activityLauncherRef?.get() ?: error("No ActivityResultLauncher is available")
        onActivityResult = callback
        launcher.launch(intent)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: ActivityProvider? = null

        fun create(context: Context): ActivityProvider {
            if (instance == null) {
                instance = ActivityProvider(context)
            }

            return instance!!
        }

        fun getInstance(): ActivityProvider = instance
            ?: error("ActivityProvider has not been initialized")
    }
}
