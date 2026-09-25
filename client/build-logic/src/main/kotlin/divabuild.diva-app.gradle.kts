plugins {
    id("divabuild.diva-app-version")
    id("divabuild.library-app-shared")
}

kotlin {
    android {
        namespace = "com.diva.app.${project.name.replace("-", ".")}"
    }
}
