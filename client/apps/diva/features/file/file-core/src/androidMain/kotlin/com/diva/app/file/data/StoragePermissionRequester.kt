package com.diva.app.file.data

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import io.github.juevigrace.diva.core.permissions.AndroidPermissionRequester
import io.github.juevigrace.diva.core.permissions.PermissionState
import io.github.juevigrace.diva.core.permissions.PermissionStatus

private const val MANAGE_EXTERNAL_STORAGE = "android.permission.MANAGE_EXTERNAL_STORAGE"

/**
 * Storage access is granted through the legacy runtime permission up to API 29 and through
 * the all-files access (`MANAGE_EXTERNAL_STORAGE`) from API 30, which has no runtime dialog
 * and can only be granted from the Settings screen.
 */
class StoragePermissionRequester : AndroidPermissionRequester() {
    override val requiredPermissions: List<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            listOf(MANAGE_EXTERNAL_STORAGE)
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    override fun checkPermission(permission: String): PermissionState {
        if (permission == MANAGE_EXTERNAL_STORAGE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return PermissionState(mapOf(permission to manageStatus()))
        }

        return super.checkPermission(permission)
    }

    override suspend fun request(): PermissionState {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                openAllFilesAccessSettings()
            }
            return PermissionState(mapOf(MANAGE_EXTERNAL_STORAGE to manageStatus()))
        }

        return super.request()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun manageStatus(): PermissionStatus =
        if (Environment.isExternalStorageManager()) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.RequiresSettings
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private suspend fun openAllFilesAccessSettings() {
        startActivityForResult { context ->
            Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.fromParts("package", context.packageName, null),
            )
        }
    }
}
