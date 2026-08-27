plugins {
    base
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

tasks.named("clean") {
    dependsOn(cleanAll)
}
