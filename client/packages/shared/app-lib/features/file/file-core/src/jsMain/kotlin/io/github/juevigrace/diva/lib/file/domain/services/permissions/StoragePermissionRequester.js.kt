package io.github.juevigrace.diva.lib.file.domain.services.permissions

import io.github.juevigrace.diva.core.permissions.PermissionRequester
import io.github.juevigrace.diva.core.permissions.PermissionState
import io.github.juevigrace.diva.core.permissions.PermissionStatus

actual object StoragePermissionRequester : PermissionRequester {
    actual override val requiredPermissions: List<String>
        get() = emptyList()

    actual override fun checkPermission(permission: String): PermissionState =
        PermissionState(mapOf(permission to PermissionStatus.Granted))

    actual override suspend fun request(): PermissionState =
        PermissionState(emptyMap())
}
