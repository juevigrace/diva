import divabuild.internal.libs
import divabuild.internal.moduleNamespace

plugins {
    id("divabuild.library-app-shared")
}

version = libs.versions.diva.app.name

kotlin {
    android {
        namespace = project.moduleNamespace("com.diva.app")
    }
}
