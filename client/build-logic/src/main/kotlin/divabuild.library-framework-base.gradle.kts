plugins {
    id("divabuild.library-base")
    id("divabuild.library-version-framework")
}

group = "io.github.juevigrace"

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.split("-").joinToString(".")}"
    }
}
