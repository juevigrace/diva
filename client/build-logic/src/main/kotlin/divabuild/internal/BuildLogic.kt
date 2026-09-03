package divabuild.internal

import org.gradle.api.Project
import java.io.File

internal fun Project.buildLogicResourcesDir(): File {
    var dir: File? = rootProject.projectDir
    while (dir != null && dir.isDirectory) {
        val candidate = File(dir, "build-logic/src/main/resources")
        if (File(candidate, "consumer-rules.pro").isFile) {
            return candidate
        }
        dir = dir.parentFile
    }
    error(
        "Unable to locate 'build-logic/src/main/resources' by walking up from " +
            "root project '${rootProject.projectDir}'. Ensure the build-logic module exists.",
    )
}
