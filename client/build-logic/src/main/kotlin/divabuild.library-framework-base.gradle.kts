plugins {
    id("divabuild.library-base")
    id("divabuild.library-version-framework")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.split("-").joinToString(".")}"
    }
}
