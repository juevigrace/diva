package io.github.juevigrace.diva.lib.file.domain.services.permissions

import io.github.juevigrace.diva.core.permissions.PermissionRequester
import io.github.juevigrace.diva.core.permissions.PermissionState

expect object StoragePermissionRequester : PermissionRequester {
    override val requiredPermissions: List<String>
    override fun checkPermission(permission: String): PermissionState
    override suspend fun request(): PermissionState
}
