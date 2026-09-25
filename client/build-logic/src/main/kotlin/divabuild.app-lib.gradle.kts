import divabuild.internal.dottedModuleId
import divabuild.internal.libs
import divabuild.internal.moduleSlug

plugins {
    id("divabuild.library-app")
}

version = libs.versions.diva.lib

base {
    archivesName.set("diva-lib-${project.moduleSlug()}")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${base.archivesName.get().dottedModuleId()}"
    }
}
