import divabuild.internal.libs
import divabuild.internal.moduleNamespace

plugins {
    id("divabuild.library-base")
    id("divabuild.targets-web")
}

version = libs.versions.diva.framework

kotlin {
    android {
        namespace = project.moduleNamespace("io.github.juevigrace")
    }
}
