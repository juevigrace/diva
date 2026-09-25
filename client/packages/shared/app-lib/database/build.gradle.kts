@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("divabuild.app-lib")
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
            implementation(projects.core)

            implementation(projects.features.user.userModels)
            implementation(projects.features.permissions.permissionsModels)

            implementation(projects.features.devices.devicesDatabase)
            implementation(projects.features.permissions.permissionsDatabase)
            implementation(projects.features.session.sessionDatabase)
            implementation(projects.features.user.userDatabase)
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
            dependency(project(":features:session:session-database"))
            dependency(project(":features:user:user-database"))
            dependency(project(":features:permissions:permissions-database"))
            dependency(project(":features:devices:devices-database"))
        }
    }
}
