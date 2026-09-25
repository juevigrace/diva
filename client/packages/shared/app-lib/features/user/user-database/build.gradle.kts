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
            api(libs.diva.database.sqlite)

            implementation(projects.core)

            implementation(projects.features.user.userModels)
            implementation(projects.features.devices.devicesModels)
            implementation(projects.features.permissions.permissionsModels)

            implementation(projects.features.devices.devicesDatabase)
            implementation(projects.features.permissions.permissionsDatabase)
        }
    }
}

sqldelight {
    databases {
        register("DivaSharedDB") {
            packageName.set("io.github.juevigrace.diva.lib.database.user")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)

            dependency(project(":features:permissions:permissions-database"))
            dependency(project(":features:devices:devices-database"))
        }
    }
}
