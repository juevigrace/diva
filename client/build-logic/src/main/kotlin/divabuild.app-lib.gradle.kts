plugins {
    base
    id("divabuild.library-version-apps")
    id("divabuild.library-app")
}

val moduleSlug =
    project.path.removePrefix(":").split(":").filter { it != "features" }.let { path ->
        if (path.size == 1) {
            path.first()
        } else {
            val feature = path.first()
            val submodule = path.drop(1).joinToString("-").removePrefix("$feature-")
            if (submodule == "core") feature else "$feature-$submodule"
        }
    }

base {
    archivesName.set("diva-lib-$moduleSlug")
}

kotlin {
    android {
        namespace = "io.github.juevigrace.${base.archivesName.get().replace('-', '.')}"
    }
}
