package io.github.juevigrace.diva.core.permissions

abstract class IosPermissionRequester : PermissionRequester {
    override fun checkPermission(permission: String): PermissionState =
        PermissionState(mapOf(permission to PermissionStatus.Granted))

    override suspend fun request(): PermissionState = PermissionState(emptyMap())
}
