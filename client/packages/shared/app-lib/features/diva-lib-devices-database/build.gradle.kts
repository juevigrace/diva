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
            api(libs.diva.database.sqlite)

            implementation(projects.divaLibCore)
            implementation(projects.divaLibModelsCore)
        }
    }
}

sqldelight {
    databases {
        register("DivaSharedDB") {
            packageName.set("io.github.juevigrace.diva.lib.database.devices")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
            generateAsync.set(true)
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
        }
    }
}