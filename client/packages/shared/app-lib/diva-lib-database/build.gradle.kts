@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.library-app")
    alias(libs.plugins.sqldelight)
}

kotlin {
    js {
        browser()
        binaries.library()
    }

    wasmJs {
        browser()
        binaries.library()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.divaLibCore)
            implementation(projects.divaLibModelsCore)

            implementation(projects.features.divaLibDevicesDatabase)
            implementation(projects.features.divaLibPermissionsDatabase)
            implementation(projects.features.divaLibSessionDatabase)
            implementation(projects.features.divaLibUserDatabase)
        }
    }
}

sqldelight {
    databases {
        register("DivaSharedDB") {
            packageName.set("io.github.juevigrace.diva.lib.database")
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
            dependency(project(":features:diva-lib-session-database"))
            dependency(project(":features:diva-lib-user-database"))
            dependency(project(":features:diva-lib-permissions-database"))
            dependency(project(":features:diva-lib-devices-database"))
        }
    }
}
