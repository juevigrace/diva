package io.github.juevigrace.diva.core.permissions

abstract class JvmPermissionRequester : PermissionRequester {
    override fun checkPermission(permission: String): PermissionState =
        PermissionState(mapOf(permission to PermissionStatus.Granted))

    override suspend fun request(): PermissionState = PermissionState(emptyMap())
}
