package io.github.juevigrace.diva.core.permissions

interface PermissionRequester {

    /** Every permission that must be granted before the guarded operation runs. */
    val requiredPermissions: List<String>

    /** True only when all of [requiredPermissions] are currently granted. */
    val hasPermission: Boolean
        get() = requiredPermissions.all { checkPermission(it).hasPermissions }

    /** Checks a single permission without prompting, mirroring `checkSelfPermission`. */
    fun checkPermission(permission: String): PermissionState

    /** Prompts for every one of [requiredPermissions] and reports the outcome. */
    suspend fun request(): PermissionState
}

data class PermissionState(
    val permissions: Map<String, PermissionStatus>,
) {
    val hasPermissions: Boolean
        get() = permissions.values.all { it == PermissionStatus.Granted }

    /** True when at least one permission can only be granted from a settings screen. */
    val requiresSettings: Boolean
        get() = permissions.values.any { it == PermissionStatus.RequiresSettings }
}

enum class PermissionStatus {
    Unspecified,
    Granted,
    Denied,
    DeniedForever,

    /** The permission has no runtime dialog and can only be granted from a settings screen. */
    RequiresSettings,
}

/** Folds per-permission states into the most severe outcome. */
fun Iterable<PermissionStatus>.aggregate(): PermissionStatus = when {
    all { it == PermissionStatus.Granted } -> PermissionStatus.Granted
    any { it == PermissionStatus.DeniedForever } -> PermissionStatus.DeniedForever
    any { it == PermissionStatus.RequiresSettings } -> PermissionStatus.RequiresSettings
    any { it == PermissionStatus.Denied } -> PermissionStatus.Denied
    else -> PermissionStatus.Unspecified
}

fun PermissionStatus.toException(): PermissionException = when (this) {
    PermissionStatus.Granted -> error("Permission is granted")
    PermissionStatus.Unspecified -> PermissionNotDeterminedException()
    PermissionStatus.Denied -> PermissionDeniedException()
    PermissionStatus.DeniedForever -> PermissionDeniedForeverException()
    PermissionStatus.RequiresSettings -> PermissionRequiresSettingsException()
}

/** Base type for permission failures. */
sealed class PermissionException(message: String) : Throwable(message)

/** The permission has never been requested nor granted. */
class PermissionNotDeterminedException :
    PermissionException("Permission has not been granted yet")

/** The user denied the permission request. */
class PermissionDeniedException : PermissionException("Permission was denied")

/** The user denied the permission request and selected "Don't ask again". */
class PermissionDeniedForeverException :
    PermissionException("Permission was denied permanently")

/** The permission can only be granted from a settings screen. */
class PermissionRequiresSettingsException :
    PermissionException("Permission can only be granted from settings")
