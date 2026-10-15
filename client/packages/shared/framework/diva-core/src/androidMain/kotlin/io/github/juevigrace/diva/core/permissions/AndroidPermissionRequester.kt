package io.github.juevigrace.diva.core.permissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.github.juevigrace.diva.core.permissions.activity.ActivityProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

abstract class AndroidPermissionRequester : PermissionRequester {
    abstract override val requiredPermissions: List<String>
    private val provider: ActivityProvider get() = ActivityProvider.getInstance()

    private val cachedStates = mutableMapOf<String, PermissionStatus>()

    override fun checkPermission(permission: String): PermissionState {
        val granted = ContextCompat.checkSelfPermission(
            provider.appContext,
            permission,
        ) == PackageManager.PERMISSION_GRANTED

        val state = if (granted) {
            PermissionStatus.Granted
        } else {
            cachedStates[permission] ?: PermissionStatus.Unspecified
        }

        return PermissionState(mapOf(permission to state))
    }

    override suspend fun request(): PermissionState {
        if (requiredPermissions.isEmpty()) {
            return PermissionState(emptyMap())
        }

        val results = suspendCancellableCoroutine { continuation ->
            provider.activity.runOnUiThread {
                provider.launch(requiredPermissions) { results ->
                    if (continuation.isActive) continuation.resume(results)
                }
            }
        }

        results.forEach { (permission, granted) ->
            cachedStates[permission] = when {
                granted -> PermissionStatus.Granted
                ActivityCompat.shouldShowRequestPermissionRationale(provider.activity, permission) ->
                    PermissionStatus.Denied
                else -> PermissionStatus.DeniedForever
            }
        }

        return PermissionState(cachedStates.toMap())
    }

    protected suspend fun startActivityForResult(buildIntent: (Context) -> Intent) {
        val intent = buildIntent(provider.appContext)
        suspendCancellableCoroutine { continuation ->
            provider.activity.runOnUiThread {
                provider.launchActivity(intent) {
                    if (continuation.isActive) continuation.resume(Unit)
                }
            }
        }
    }
}
