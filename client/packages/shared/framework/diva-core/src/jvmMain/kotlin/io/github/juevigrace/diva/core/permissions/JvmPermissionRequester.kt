package io.github.juevigrace.diva.core.permissions

class JvmPermissionRequester : PermissionRequester {
    override val requiredPermissions: List<String> = emptyList()

    override fun checkPermission(permission: String): PermissionState =
        PermissionState(mapOf(permission to PermissionStatus.Granted))

    override suspend fun request(): PermissionState = PermissionState(emptyMap())
}
