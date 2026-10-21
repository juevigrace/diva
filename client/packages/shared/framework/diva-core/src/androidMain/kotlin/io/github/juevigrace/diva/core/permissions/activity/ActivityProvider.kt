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
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private var activityLauncher: ActivityResultLauncher<Intent>? = null
    private var onResult: ((Map<String, Boolean>) -> Unit)? = null
    private var onActivityResult: (() -> Unit)? = null

    val activity: ComponentActivity
        get() = activityRef?.get() ?: error("No ComponentActivity is available")

    private val lifecycleObserver = createActivityLifecycleObserver(
        onCreated = { activity ->
            activityRef = WeakReference(activity)
            launcher = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
            ) { results ->
                val callback = onResult
                onResult = null
                callback?.invoke(results)
            }

            activityLauncher = activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) {
                val callback = onActivityResult
                onActivityResult = null
                callback?.invoke()
            }
        },
        onDestroyed = { activity ->
            if (activityRef?.get() === activity) {
                activityRef = null
                launcher = null
                activityLauncher = null
            }
        },
    )

    init {
        val application = appContext as? Application
        application?.registerActivityLifecycleCallbacks(lifecycleObserver)
            ?: run { activityRef = WeakReference(appContext as? ComponentActivity) }
    }

    fun launch(permissions: List<String>, callback: (Map<String, Boolean>) -> Unit) {
        val launcherInstance = launcher ?: error("No ActivityResultLauncher is available")
        onResult = callback
        launcherInstance.launch(permissions.toTypedArray())
    }

    fun launchActivity(intent: Intent, callback: () -> Unit) {
        val launcherInstance = activityLauncher ?: error("No ActivityResultLauncher is available")
        onActivityResult = callback
        launcherInstance.launch(intent)
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
