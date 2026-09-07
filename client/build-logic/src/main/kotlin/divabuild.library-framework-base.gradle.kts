plugins {
    id("divabuild.library-base")
    id("divabuild.library-version-framework")
    id("divabuild.targets-web")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.split("-").joinToString(".")}"
    }
}
