import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

plugins {
    base
}

abstract class BuildAllTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {
    @TaskAction
    fun build() {
        val buildRoots = listOf(
            project.projectDir.resolve("packages/shared/framework"),
            project.projectDir.resolve("packages/shared/app-lib"),
            project.projectDir.resolve("apps/diva"),
        )
        buildRoots.forEach { dir ->
            execOperations.exec {
                workingDir = dir
                commandLine("./gradlew", "build")
            }.rethrowFailure()
        }
    }
}

val cleanAll = tasks.register<Delete>("cleanAll") {
    group = "build"
    description = "Deletes all build/ directories across the composite builds."
    val rootDir = rootProject.projectDir
    delete(
        rootDir
            .walkTopDown()
            .filter { it.isDirectory && it.name == "build" }
            .toList(),
    )
}

val buildAll = tasks.register<BuildAllTask>("buildAll") {
    group = "build"
    description = "Builds all Gradle composite builds (framework, app-lib, apps/diva)."
}

tasks.named("clean") {
    dependsOn(cleanAll)
}
