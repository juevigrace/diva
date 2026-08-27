plugins {
    base
}

val cleanAll = tasks.register<Delete>("cleanAll") {
    group = "build"
    description = "Deletes all build/ and .gradle/ directories across the composite builds."
    val rootDir = rootProject.projectDir
    delete(
        rootDir
            .walkTopDown()
            .filter { it.isDirectory && (it.name == "build" || it.name == ".gradle") }
            .toList(),
    )
}

tasks.named("clean") {
    dependsOn(cleanAll)
}
