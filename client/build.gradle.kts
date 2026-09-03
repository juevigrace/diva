import org.gradle.api.DefaultTask
import org.gradle.api.file.Directory
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

plugins {
    base
}

abstract class BuildAllTask @Inject constructor(
    private val execOperations: ExecOperations,
) : DefaultTask() {
    @get:Input
    abstract val buildRoots: ListProperty<Directory>

    @TaskAction
    fun build() {
        buildRoots.get().forEach { dir ->
            execOperations.exec {
                workingDir = dir.asFile
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
    val rootDir = layout.projectDirectory
    buildRoots.set(
        listOf(
            rootDir.dir("packages/shared/framework"),
            rootDir.dir("packages/shared/app-lib"),
            rootDir.dir("apps/diva"),
        ),
    )
}

tasks.named("clean") {
    dependsOn(cleanAll)
}
