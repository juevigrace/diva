plugins {
    id("divabuild.library-version-apps")
    id("divabuild.library-app")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${project.name.replace("-", ".")}"
    }
}