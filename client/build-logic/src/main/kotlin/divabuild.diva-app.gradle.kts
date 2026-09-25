import divabuild.internal.appModuleNamespace
import divabuild.internal.libs

plugins {
    id("divabuild.library-app-shared")
}

version = libs.versions.diva.app.name

kotlin {
    android {
        namespace = project.appModuleNamespace("com.diva.app")
    }
}
