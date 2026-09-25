package divabuild.internal

import org.gradle.api.Project

internal fun Project.moduleSlug(): String =
    path.removePrefix(":").split(":").filter { it != "features" }.let { segments ->
        if (segments.size == 1) {
            segments.first()
        } else {
            val feature = segments.first()
            val submodule = segments.drop(1).joinToString("-").removePrefix("$feature-")
            if (submodule == "core") feature else "$feature-$submodule"
        }
    }

internal fun String.dottedModuleId(): String = replace('-', '.')

internal fun Project.moduleNamespace(prefix: String): String = "$prefix.${name.dottedModuleId()}"

internal fun Project.appModuleNamespace(prefix: String): String = "$prefix.${moduleSlug().dottedModuleId()}"
