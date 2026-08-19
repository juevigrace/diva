plugins {
    id("divabuild.kmp")
    id("divabuild.library-package")
    id("divabuild.targets-android")
    id("divabuild.targets-ios")
    id("divabuild.targets-jvm")
    id("divabuild.targets-web")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.split("-").joinToString(".")}"
    }
}
